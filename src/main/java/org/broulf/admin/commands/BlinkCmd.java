/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.commands;

import org.broulf.admin.command.CmdError;
import org.broulf.admin.command.CmdException;
import org.broulf.admin.command.CmdSyntaxError;
import org.broulf.admin.command.Command;
import org.broulf.admin.hacks.BlinkHack;
import org.broulf.admin.Feature;

public final class BlinkCmd extends Command
{
	public BlinkCmd()
	{
		super("blink", "Enables, disables or cancels Blink.", ".blink [on|off]",
			".blink cancel");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length > 1)
			throw new CmdSyntaxError();
		
		//BlinkHack blinkHack = Feature.WURST.getHax().blinkHack;
		
		if(args.length == 0)
		{
			//blinkHack.setEnabled(!blinkHack.isEnabled());
			return;
		}
		
		switch(args[0].toLowerCase())
		{
			default:
			throw new CmdSyntaxError();
			
			case "on":
			//blinkHack.setEnabled(true);
			break;
			
			case "off":
			//blinkHack.setEnabled(false);
			break;
			
			case "cancel":
			//cancel(blinkHack);
			break;
		}
	}
	
	private void cancel(BlinkHack blinkHack) throws CmdException
	{
		if(!blinkHack.isEnabled())
			throw new CmdError("Cannot cancel, Blink is already turned off!");
		
		blinkHack.cancel();
	}
}
