/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.hacks;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.broulf.admin.Category;
import org.broulf.admin.events.UpdateListener;
import org.broulf.admin.hack.Hack;

public final class TiredHack extends Hack implements UpdateListener
{
	public TiredHack()
	{
		super("Tired", "Makes you look like Alexander\n"
			+ "back in April 2015.\n" + "Only visible to other players.");
		setCategory(Category.FUN);
	}
	
	@Override
	public void onEnable()
	{
		// disable incompatible derps
		EVENTS.add(UpdateListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		MC.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookOnly(
			MC.player.yaw, MC.player.age % 100, MC.player.isOnGround()));
	}
}
