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
import org.broulf.admin.settings.SliderSetting;

@SearchTags({"high jump"})
public final class HighJumpHack extends Hack
{
	private final SliderSetting height = new SliderSetting("Height",
		"Jump height in blocks.\n"
			+ "This gets very inaccurate at higher values.",
		6, 1, 100, 1, SliderSetting.ValueDisplay.INTEGER);
	
	public HighJumpHack()
	{
		super("HighJump", "Allows you to jump higher.\n\n"
			+ "\u00a7c\u00a7lWARNING:\u00a7r You will take fall damage if you don't use NoFall.");
		
		setCategory(Category.MOVEMENT);
		addSetting(height);
	}
	
	public float getAdditionalJumpMotion()
	{
		return isEnabled() ? height.getValueF() * 0.1F : 0;
	}
}
