/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.commands;

import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import org.broulf.admin.SearchTags;
import org.broulf.admin.command.CmdException;
import org.broulf.admin.command.CmdSyntaxError;
import org.broulf.admin.command.Command;
import org.broulf.admin.Feature;

@SearchTags({".legit", "dots in chat", "command bypass", "prefix"})
public final class SayCmd extends Command
{
	public SayCmd()
	{
		super("say",
			"Sends the given chat message, even if it starts with a\n" + "dot.",
			".say <message>");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length < 1)
			throw new CmdSyntaxError();
		
		String message = String.join(" ", args);
		ChatMessageC2SPacket packet = new ChatMessageC2SPacket(message);
		Feature.MC.getNetworkHandler().sendPacket(packet);
	}
}