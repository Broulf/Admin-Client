/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.commands;

import net.minecraft.client.network.ServerInfo;
import org.broulf.admin.command.CmdError;
import org.broulf.admin.command.CmdException;
import org.broulf.admin.command.CmdSyntaxError;
import org.broulf.admin.command.Command;
import org.broulf.admin.util.ChatUtils;
import org.broulf.admin.util.LastServerRememberer;
import org.broulf.admin.Feature;

public final class SvCmd extends Command
{
	public SvCmd()
	{
		super("sv", "Shows the version of the server\n"
			+ "you are currently connected to.", ".sv");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length != 0)
			throw new CmdSyntaxError();
		
		ChatUtils.message("Server version: " + getVersion());
	}
	
	private String getVersion() throws CmdError
	{
		if(Feature.MC.isIntegratedServerRunning())
			throw new CmdError("Can't check server version in singleplayer.");
		
		ServerInfo lastServer = LastServerRememberer.getLastServer();
		if(lastServer == null)
			throw new IllegalStateException(
				"LastServerRememberer doesn't remember the last server!");
		
		return lastServer.version.getString();
	}
	
	@Override
	public String getPrimaryAction()
	{
		return "Get Server Version";
	}
	
	@Override
	public void doPrimaryAction()
	{
		Feature.WURST.getCmdProcessor().process("sv");
	}
}
