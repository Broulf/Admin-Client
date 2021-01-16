/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.commands;

import java.util.Comparator;
import java.util.stream.StreamSupport;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import org.broulf.admin.ai.PathFinder;
import org.broulf.admin.ai.PathProcessor;
import org.broulf.admin.command.CmdError;
import org.broulf.admin.command.CmdException;
import org.broulf.admin.command.CmdSyntaxError;
import org.broulf.admin.command.Command;
import org.broulf.admin.events.RenderListener;
import org.broulf.admin.events.UpdateListener;
import org.broulf.admin.util.ChatUtils;
import org.broulf.admin.util.FakePlayerEntity;
import org.broulf.admin.util.MathUtils;
import org.broulf.admin.Feature;

public final class GoToCmd extends Command
	implements UpdateListener, RenderListener
{
	private PathFinder pathFinder;
	private PathProcessor processor;
	private boolean enabled;
	
	public GoToCmd()
	{
		super("goto", "Walks or flies you to a specific location.",
			".goto <x> <y> <z>", ".goto <entity>", ".goto -path",
			"Turn off: .goto");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		// disable if enabled
		if(enabled)
		{
			disable();
			
			if(args.length == 0)
				return;
		}
		
		// set PathFinder
		if(args.length == 1 && args[0].equals("-path"))
		{
			BlockPos goal = Feature.WURST.getCmds().pathCmd.getLastGoal();
			if(goal != null)
				pathFinder = new PathFinder(goal);
			else
				throw new CmdError("No previous position on .path.");
		}else
		{
			BlockPos goal = argsToPos(args);
			pathFinder = new PathFinder(goal);
		}
		
		// start
		enabled = true;
		Feature.EVENTS.add(UpdateListener.class, this);
		Feature.EVENTS.add(RenderListener.class, this);
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
		// find path
		if(!pathFinder.isDone())
		{
			PathProcessor.lockControls();
			
			pathFinder.think();
			
			if(!pathFinder.isDone())
			{
				if(pathFinder.isFailed())
				{
					ChatUtils.error("Could not find a path.");
					disable();
				}
				
				return;
			}
			
			pathFinder.formatPath();
			
			// set processor
			processor = pathFinder.getProcessor();
			
			System.out.println("Done");
		}
		
		// check path
		if(processor != null
			&& !pathFinder.isPathStillValid(processor.getIndex()))
		{
			System.out.println("Updating path...");
			pathFinder = new PathFinder(pathFinder.getGoal());
			return;
		}
		
		// process path
		processor.process();
		
		if(processor.isDone())
			disable();
	}
	
	@Override
	public void onRender(float partialTicks)
	{
		PathCmd pathCmd = Feature.WURST.getCmds().pathCmd;
		pathFinder.renderPath(pathCmd.isDebugMode(), pathCmd.isDepthTest());
	}
	
	private void disable()
	{
		Feature.EVENTS.remove(UpdateListener.class, this);
		Feature.EVENTS.remove(RenderListener.class, this);
		
		pathFinder = null;
		processor = null;
		PathProcessor.releaseControls();
		
		enabled = false;
	}
	
	public boolean isActive()
	{
		return enabled;
	}
}
