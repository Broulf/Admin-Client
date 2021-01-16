/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.commands;

import org.broulf.admin.command.CmdException;
import org.broulf.admin.command.Command;
import org.broulf.admin.Feature;

public final class BindCmd extends Command
{
	public BindCmd()
	{
		super("bind", "Shortcut for '.binds add'.", ".bind <key> <hacks>",
			".bind <key> <commands>",
			"Multiple hacks/commands must be separated by ';'.",
			"Use .binds for more options.");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		Feature.WURST.getCmdProcessor().process("binds add " + String.join(" ", args));
	}
}
