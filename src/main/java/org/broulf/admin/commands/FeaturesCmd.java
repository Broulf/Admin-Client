/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.commands;

import org.broulf.admin.AdminClient;
import org.broulf.admin.command.CmdException;
import org.broulf.admin.command.CmdSyntaxError;
import org.broulf.admin.command.Command;
import org.broulf.admin.hack.Hack;
import org.broulf.admin.other_feature.OtherFeature;
import org.broulf.admin.util.ChatUtils;
import org.broulf.admin.Feature;

public final class FeaturesCmd extends Command
{
	public FeaturesCmd()
	{
		super("features",
			"Shows the number of features and some other\n" + "statistics.",
			".features");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length != 0)
			throw new CmdSyntaxError();
		
		if(AdminClient.VERSION.startsWith("7.0pre"))
			ChatUtils.warning(
				"This is just a pre-release! It doesn't (yet) have all of the features of Wurst 7.0! See download page for details.");
		
		int hax = Feature.WURST.getHax().countHax();
		int cmds = Feature.WURST.getCmds().countCmds();
		int otfs = Feature.WURST.getOtfs().countOtfs();
		int all = hax + cmds + otfs;
		
		ChatUtils.message("All features: " + all);
		ChatUtils.message("Hacks: " + hax);
		ChatUtils.message("Commands: " + cmds);
		ChatUtils.message("Other features: " + otfs);
		
		int settings = 0;
		for(Hack hack : Feature.WURST.getHax().getAllHax())
			settings += hack.getSettings().size();
		for(Command cmd : Feature.WURST.getCmds().getAllCmds())
			settings += cmd.getSettings().size();
		for(OtherFeature otf : Feature.WURST.getOtfs().getAllOtfs())
			settings += otf.getSettings().size();
		
		ChatUtils.message("Settings: " + settings);
	}
	
	@Override
	public String getPrimaryAction()
	{
		return "Show Statistics";
	}
	
	@Override
	public void doPrimaryAction()
	{
		Feature.WURST.getCmdProcessor().process("features");
	}
}
