/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.commands;

import java.util.ArrayList;

import org.broulf.admin.command.CmdError;
import org.broulf.admin.command.CmdException;
import org.broulf.admin.command.CmdSyntaxError;
import org.broulf.admin.command.Command;
import org.broulf.admin.settings.CheckboxSetting;
import org.broulf.admin.util.ChatUtils;
import org.broulf.admin.util.MathUtils;
import org.broulf.admin.Feature;

public class FriendsCmd extends Command
{
	private static final int FRIENDS_PER_PAGE = 8;
	
	private final CheckboxSetting middleClickFriends = new CheckboxSetting(
		"Middle click friends", "Add/remove friends by clicking them with\n"
			+ "the middle mouse button.",
		true);
	
	public FriendsCmd()
	{
		super("friends", "Manages your friends list.", ".friends add <name>",
			".friends remove <name>", ".friends remove-all",
			".friends list [<page>]");
		
		addSetting(middleClickFriends);
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length < 1 || args.length > 2)
			throw new CmdSyntaxError();
		
		switch(args[0].toLowerCase())
		{
			case "add":
			add(args);
			break;
			
			case "remove":
			remove(args);
			break;
			
			case "remove-all":
			removeAll(args);
			break;
			
			case "list":
			list(args);
			break;
			
			default:
			throw new CmdSyntaxError();
		}
	}
	
	private void add(String[] args) throws CmdException
	{
		if(args.length != 2)
			throw new CmdSyntaxError();
		
		String name = args[1];
		if(Feature.WURST.getFriends().contains(name))
			throw new CmdError(
				"\"" + name + "\" is already in your friends list.");
		
		Feature.WURST.getFriends().addAndSave(name);
		ChatUtils.message("Added friend \"" + name + "\".");
	}
	
	private void remove(String[] args) throws CmdException
	{
		if(args.length != 2)
			throw new CmdSyntaxError();
		
		String name = args[1];
		if(!Feature.WURST.getFriends().contains(name))
			throw new CmdError("\"" + name + "\" is not in your friends list.");
		
		Feature.WURST.getFriends().removeAndSave(name);
		ChatUtils.message("Removed friend \"" + name + "\".");
	}
	
	private void removeAll(String[] args) throws CmdException
	{
		if(args.length > 1)
			throw new CmdSyntaxError();
		
		Feature.WURST.getFriends().removeAllAndSave();
		ChatUtils.message("All friends removed. Oof.");
	}
	
	private void list(String[] args) throws CmdException
	{
		if(args.length > 2)
			throw new CmdSyntaxError();
		
		ArrayList<String> friends = Feature.WURST.getFriends().toList();
		int page = parsePage(args);
		int pages = (int)Math.ceil(friends.size() / (double)FRIENDS_PER_PAGE);
		pages = Math.max(pages, 1);
		
		if(page > pages || page < 1)
			throw new CmdSyntaxError("Invalid page: " + page);
		
		ChatUtils.message("Current friends: " + friends.size());
		
		int start = (page - 1) * FRIENDS_PER_PAGE;
		int end = Math.min(page * FRIENDS_PER_PAGE, friends.size());
		
		ChatUtils.message("Friends list (page " + page + "/" + pages + ")");
		for(int i = start; i < end; i++)
			ChatUtils.message(friends.get(i).toString());
	}
	
	private int parsePage(String[] args) throws CmdSyntaxError
	{
		if(args.length < 2)
			return 1;
		
		if(!MathUtils.isInteger(args[1]))
			throw new CmdSyntaxError("Not a number: " + args[1]);
		
		return Integer.parseInt(args[1]);
	}
	
	public CheckboxSetting getMiddleClickFriends()
	{
		return middleClickFriends;
	}
}
