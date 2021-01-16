/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.commands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.StreamSupport;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import org.broulf.admin.ai.PathFinder;
import org.broulf.admin.ai.PathPos;
import org.broulf.admin.command.CmdError;
import org.broulf.admin.command.CmdException;
import org.broulf.admin.command.CmdSyntaxError;
import org.broulf.admin.command.Command;
import org.broulf.admin.events.RenderListener;
import org.broulf.admin.events.UpdateListener;
import org.broulf.admin.settings.CheckboxSetting;
import org.broulf.admin.util.ChatUtils;
import org.broulf.admin.util.FakePlayerEntity;
import org.broulf.admin.util.MathUtils;
import org.broulf.admin.Feature;

public final class PathCmd extends Command
	implements UpdateListener, RenderListener
{
	private final CheckboxSetting debugMode =
		new CheckboxSetting("Debug mode", false);
	
	private final CheckboxSetting depthTest =
		new CheckboxSetting("Depth test", false);
	
	private PathFinder pathFinder;
	private boolean enabled;
	private long startTime;
	private BlockPos lastGoal;
	
	public PathCmd()
	{
		super("path",
			"Shows the shortest path to a specific point.\n"
				+ "Useful for labyrinths and caves.",
			".path <x> <y> <z>", ".path <entity>", ".path -debug",
			".path -depth", ".path -refresh", "Turn off: .path");
		
		addSetting(debugMode);
		addSetting(depthTest);
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		// process special commands
		boolean refresh = false;
		if(args.length > 0 && args[0].startsWith("-"))
			switch(args[0])
			{
				case "-debug":
				debugMode.setChecked(!debugMode.isChecked());
				ChatUtils.message("Debug mode "
					+ (debugMode.isChecked() ? "on" : "off") + ".");
				return;
				
				case "-depth":
				depthTest.setChecked(!depthTest.isChecked());
				ChatUtils.message("Depth test "
					+ (depthTest.isChecked() ? "on" : "off") + ".");
				return;
				
				case "-refresh":
				if(lastGoal == null)
					throw new CmdError("Cannot refresh: no previous path.");
				refresh = true;
				break;
			}
		
		// disable if enabled
		if(enabled)
		{
			Feature.EVENTS.remove(UpdateListener.class, this);
			Feature.EVENTS.remove(RenderListener.class, this);
			enabled = false;
			
			if(args.length == 0)
				return;
		}
		
		// set PathFinder
		final BlockPos goal;
		if(refresh)
			goal = lastGoal;
		else
		{
			goal = argsToPos(args);
			lastGoal = goal;
		}
		pathFinder = new PathFinder(goal);
		
		// start
		enabled = true;
		Feature.EVENTS.add(UpdateListener.class, this);
		Feature.EVENTS.add(RenderListener.class, this);
		System.out.println("Finding path...");
		startTime = System.nanoTime();
	}
	
	private BlockPos argsToPos(String... args) throws CmdException
	{
		switch(args.length)
		{
			default:
			throw new CmdSyntaxError("Invalid coordinates.");
			
			case 1:
			return argsToEntityPos(args[0]);
			
			case 3:
			return argsToXyzPos(args);
		}
	}
	
	private BlockPos argsToEntityPos(String name) throws CmdError
	{
		LivingEntity entity = StreamSupport
			.stream(Feature.MC.world.getEntities().spliterator(), true)
			.filter(e -> e instanceof LivingEntity).map(e -> (LivingEntity)e)
			.filter(e -> !e.removed && e.getHealth() > 0)
			.filter(e -> e != Feature.MC.player)
			.filter(e -> !(e instanceof FakePlayerEntity))
			.filter(e -> name.equalsIgnoreCase(e.getDisplayName().getString()))
			.min(
				Comparator.comparingDouble(e -> Feature.MC.player.squaredDistanceTo(e)))
			.orElse(null);
		
		if(entity == null)
			throw new CmdError("Entity \"" + name + "\" could not be found.");
		
		return new BlockPos(entity.getPos());
	}
	
	private BlockPos argsToXyzPos(String... xyz) throws CmdSyntaxError
	{
		BlockPos playerPos = new BlockPos(Feature.MC.player.getPos());
		int[] player =
			new int[]{playerPos.getX(), playerPos.getY(), playerPos.getZ()};
		int[] pos = new int[3];
		
		for(int i = 0; i < 3; i++)
			if(MathUtils.isInteger(xyz[i]))
				pos[i] = Integer.parseInt(xyz[i]);
			else if(xyz[i].equals("~"))
				pos[i] = player[i];
			else if(xyz[i].startsWith("~")
				&& MathUtils.isInteger(xyz[i].substring(1)))
				pos[i] = player[i] + Integer.parseInt(xyz[i].substring(1));
			else
				throw new CmdSyntaxError("Invalid coordinates.");
			
		return new BlockPos(pos[0], pos[1], pos[2]);
	}
	
	@Override
	public void onUpdate()
	{
		double passedTime = (System.nanoTime() - startTime) / 1e6;
		pathFinder.think();
		boolean foundPath = pathFinder.isDone();
		
		// stop if done or failed
		if(foundPath || pathFinder.isFailed())
		{
			ArrayList<PathPos> path = new ArrayList<>();
			if(foundPath)
				path = pathFinder.formatPath();
			else
				ChatUtils.error("Could not find a path.");
			
			Feature.EVENTS.remove(UpdateListener.class, this);
			
			System.out.println("Done after " + passedTime + "ms");
			if(debugMode.isChecked())
				System.out.println("Length: " + path.size() + ", processed: "
					+ pathFinder.countProcessedBlocks() + ", queue: "
					+ pathFinder.getQueueSize() + ", cost: "
					+ pathFinder.getCost(pathFinder.getCurrentPos()));
		}
	}
	
	@Override
	public void onRender(float partialTicks)
	{
		pathFinder.renderPath(debugMode.isChecked(), depthTest.isChecked());
	}
	
	public BlockPos getLastGoal()
	{
		return lastGoal;
	}
	
	public boolean isDebugMode()
	{
		return debugMode.isChecked();
	}
	
	public boolean isDepthTest()
	{
		return depthTest.isChecked();
	}
}
