/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.commands;

import org.broulf.admin.command.CmdException;
import org.broulf.admin.command.CmdSyntaxError;
import org.broulf.admin.command.Command;
import org.broulf.admin.events.UpdateListener;
import org.broulf.admin.Feature;

public final class DropCmd extends Command implements UpdateListener
{
	private int slowModeTimer;
	private int slowModeSlotCounter;
	
	public DropCmd()
	{
		super("drop", "Drops all your items on the ground.", ".drop",
			"Slow mode: .drop slow",
			"If regular .drop kicks you from the server,",
			"use slow mode instead.");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length > 1)
			throw new CmdSyntaxError();
		
		if(args.length < 1)
		{
			dropAllItems();
			return;
		}
		
		if(!args[0].equalsIgnoreCase("slow"))
			throw new CmdSyntaxError();
		
		slowModeTimer = 5;
		slowModeSlotCounter = 9;
		Feature.EVENTS.add(UpdateListener.class, this);
	}
	
	private void dropAllItems()
	{
		for(int i = 9; i < 45; i++)
			Feature.IMC.getInteractionManager().windowClick_THROW(i);
	}
	
	@Override
	public void onUpdate()
	{
		slowModeTimer--;
		if(slowModeTimer > 0)
			return;
		
		skipEmptySlots();
		Feature.IMC.getInteractionManager().windowClick_THROW(slowModeSlotCounter);
		
		slowModeSlotCounter++;
		slowModeTimer = 5;
		
		if(slowModeSlotCounter >= 45)
			Feature.EVENTS.remove(UpdateListener.class, this);
	}
	
	private void skipEmptySlots()
	{
		while(slowModeSlotCounter < 45)
		{
			int adjustedSlot = slowModeSlotCounter;
			if(adjustedSlot >= 36)
				adjustedSlot -= 36;
			
			if(!Feature.MC.player.inventory.getStack(adjustedSlot).isEmpty())
				break;
			
			slowModeSlotCounter++;
		}
	}
}
