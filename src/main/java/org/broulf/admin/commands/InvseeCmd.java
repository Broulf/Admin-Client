/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.commands;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import org.broulf.admin.command.CmdException;
import org.broulf.admin.command.CmdSyntaxError;
import org.broulf.admin.command.Command;
import org.broulf.admin.events.RenderListener;
import org.broulf.admin.util.ChatUtils;
import org.broulf.admin.Feature;

public final class InvseeCmd extends Command implements RenderListener
{
	private String targetName;
	
	public InvseeCmd()
	{
		super("invsee",
			"Allows you to see parts of another player's inventory.",
			".invsee <player>");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length != 1)
			throw new CmdSyntaxError();
		
		if(Feature.MC.player.abilities.creativeMode)
		{
			ChatUtils.error("Survival mode only.");
			return;
		}
		
		targetName = args[0];
		Feature.EVENTS.add(RenderListener.class, this);
	}
	
	@Override
	public void onRender(float partialTicks)
	{
		boolean found = false;
		
		for(Entity entity : Feature.MC.world.getEntities())
		{
			if(!(entity instanceof OtherClientPlayerEntity))
				continue;
			
			OtherClientPlayerEntity player = (OtherClientPlayerEntity)entity;
			String otherPlayerName = player.getName().getString();
			if(!otherPlayerName.equalsIgnoreCase(targetName))
				continue;
			
			ChatUtils.message("Showing inventory of " + otherPlayerName + ".");
			Feature.MC.openScreen(new InventoryScreen(player));
			found = true;
			break;
		}
		
		if(!found)
			ChatUtils.error("Player not found.");
		
		targetName = null;
		Feature.EVENTS.remove(RenderListener.class, this);
	}
}
