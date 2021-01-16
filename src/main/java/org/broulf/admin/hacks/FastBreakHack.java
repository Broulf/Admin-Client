/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.hacks;

import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.broulf.admin.Category;
import org.broulf.admin.SearchTags;
import org.broulf.admin.events.BlockBreakingProgressListener;
import org.broulf.admin.events.UpdateListener;
import org.broulf.admin.hack.Hack;
import org.broulf.admin.mixinterface.IClientPlayerInteractionManager;
import org.broulf.admin.settings.CheckboxSetting;
import org.broulf.admin.Feature;

@SearchTags({"FastMine", "SpeedMine", "SpeedyGonzales", "fast break",
	"fast mine", "speed mine", "speedy gonzales", "NoBreakDelay",
	"no break delay"})
public final class FastBreakHack extends Hack
	implements UpdateListener, BlockBreakingProgressListener
{
	private final CheckboxSetting legitMode = new CheckboxSetting("Legit mode",
		"Only removes the delay between breaking blocks,\n"
			+ "without speeding up the breaking process itself.\n\n"
			+ "This is slower, but usually bypasses anti-cheat\n"
			+ "plugins. Use it if regular FastBreak is not\n" + "working.",
		false);
	
	public FastBreakHack()
	{
		super("FastBreak", "Allows you to break blocks faster.\n"
			+ "Tip: This works with Nuker.");
		setCategory(Category.BLOCKS);
		addSetting(legitMode);
	}
	
	@Override
	public String getRenderName()
	{
		if(legitMode.isChecked())
			return getName() + "Legit";
		else
			return getName();
	}
	
	@Override
	protected void onEnable()
	{
		Feature.EVENTS.add(UpdateListener.class, this);
		Feature.EVENTS.add(BlockBreakingProgressListener.class, this);
	}
	
	@Override
	protected void onDisable()
	{
		Feature.EVENTS.remove(UpdateListener.class, this);
		Feature.EVENTS.remove(BlockBreakingProgressListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		Feature.IMC.getInteractionManager().setBlockHitDelay(0);
	}
	
	@Override
	public void onBlockBreakingProgress(BlockBreakingProgressEvent event)
	{
		if(legitMode.isChecked())
			return;
		
		IClientPlayerInteractionManager im = Feature.IMC.getInteractionManager();
		
		if(im.getCurrentBreakingProgress() >= 1)
			return;
		
		Action action = PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK;
		BlockPos blockPos = event.getBlockPos();
		Direction direction = event.getDirection();
		im.sendPlayerActionC2SPacket(action, blockPos, direction);
	}
}
