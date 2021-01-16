/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.hacks;

import org.broulf.admin.DontBlock;
import org.broulf.admin.SearchTags;
import org.broulf.admin.hack.DontSaveState;
import org.broulf.admin.hack.Hack;
import org.broulf.admin.navigator.NavigatorMainScreen;

@DontSaveState
@DontBlock
@SearchTags({"ClickGUI", "click gui", "SearchGUI", "search gui", "HackMenu",
	"hack menu"})
public final class NavigatorHack extends Hack
{
	public NavigatorHack()
	{
		super("Navigator",
			"A searchable GUI that learns your preferences over time.");
	}
	
	@Override
	public void onEnable()
	{
		if(!(MC.currentScreen instanceof NavigatorMainScreen))
			MC.openScreen(new NavigatorMainScreen());
		
		setEnabled(false);
	}
}
