/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.hacks;

import org.broulf.admin.Category;
import org.broulf.admin.SearchTags;
import org.broulf.admin.hack.Hack;

@SearchTags({"range"})
public final class ReachHack extends Hack
{
	public ReachHack()
	{
		super("Reach", "Allows you to reach further.");
		setCategory(Category.OTHER);
	}
	
	@Override
	public void onEnable()
	{
		IMC.getInteractionManager().setOverrideReach(true);
	}
	
	@Override
	public void onDisable()
	{
		IMC.getInteractionManager().setOverrideReach(false);
	}
}
