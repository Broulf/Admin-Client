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
import org.broulf.admin.Feature;

public final class LeaveCmd extends Command
{
	public LeaveCmd()
	{
		super("leave", "Instantly disconnects from the server.", ".leave");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length == 1 && args[0].equalsIgnoreCase("taco"))
			for(int i = 0; i < 128; i++)
				Feature.MC.player.sendChatMessage("Taco!");
		else if(args.length != 0)
			throw new CmdSyntaxError();
		
		Feature.MC.world.disconnect();
	}
	
	@Override
	public String getPrimaryAction()
	{
		return "Leave";
	}
	
	@Override
	public void doPrimaryAction()
	{
		Feature.WURST.getCmdProcessor().process("leave");
	}
}
