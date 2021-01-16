/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.commands;

import org.broulf.admin.DontBlock;
import org.broulf.admin.command.CmdError;
import org.broulf.admin.command.CmdException;
import org.broulf.admin.command.CmdSyntaxError;
import org.broulf.admin.command.Command;
import org.broulf.admin.hack.Hack;
import org.broulf.admin.hacks.TooManyHaxHack;
import org.broulf.admin.util.ChatUtils;
import org.broulf.admin.Feature;

@DontBlock
public final class TCmd extends Command
{
	public TCmd()
	{
		super("t", "Toggles a hack.", ".t <hack> [on|off]", "Examples:",
			"Toggle Nuker: .t Nuker", "Disable Nuker: .t Nuker off");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length < 1 || args.length > 2)
			throw new CmdSyntaxError();
		
		Hack hack = Feature.WURST.getHax().getHackByName(args[0]);
		if(hack == null)
			throw new CmdError("Unknown hack: " + args[0]);
		
		if(args.length == 1)
			setEnabled(hack, !hack.isEnabled());
		else
			switch(args[1].toLowerCase())
			{
				case "on":
				setEnabled(hack, true);
				break;
				
				case "off":
				setEnabled(hack, false);
				break;
				
				default:
				throw new CmdSyntaxError();
			}
	}
	
	private void setEnabled(Hack hack, boolean enabled)
	{
		TooManyHaxHack tooManyHax = Feature.WURST.getHax().tooManyHaxHack;
		if(!hack.isEnabled() && tooManyHax.isEnabled()
			&& tooManyHax.isBlocked(hack))
		{
			ChatUtils.error(hack.getName() + " is blocked by TooManyHax.");
			return;
		}
		
		hack.setEnabled(enabled);
	}
}
