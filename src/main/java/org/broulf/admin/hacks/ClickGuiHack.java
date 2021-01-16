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
import org.broulf.admin.clickgui.screens.ClickGuiScreen;
import org.broulf.admin.hack.DontSaveState;
import org.broulf.admin.hack.Hack;
import org.broulf.admin.settings.SliderSetting;

@DontSaveState
@DontBlock
@SearchTags({"click gui", "WindowGUI", "window gui", "HackMenu", "hack menu"})
public final class ClickGuiHack extends Hack
{
	private final SliderSetting opacity = new SliderSetting("Opacity", 0.5,
		0.15, 0.85, 0.01, SliderSetting.ValueDisplay.PERCENTAGE);
	
	private final SliderSetting bgRed = new SliderSetting("BG red",
		"Background red", 64, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER);
	private final SliderSetting bgGreen = new SliderSetting("BG green",
		"Background green", 64, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER);
	private final SliderSetting bgBlue = new SliderSetting("BG blue",
		"Background blue", 64, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER);
	
	private final SliderSetting acRed = new SliderSetting("AC red",
		"Accent red", 16, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER);
	private final SliderSetting acGreen = new SliderSetting("AC green",
		"Accent green", 16, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER);
	private final SliderSetting acBlue = new SliderSetting("AC blue",
		"Accent blue", 16, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER);
	
	public ClickGuiHack()
	{
		super("ClickGUI", "Window-based ClickGUI.");
		addSetting(opacity);
		addSetting(bgRed);
		addSetting(bgGreen);
		addSetting(bgBlue);
		addSetting(acRed);
		addSetting(acGreen);
		addSetting(acBlue);
	}
	
	@Override
	public void onEnable()
	{
		MC.openScreen(new ClickGuiScreen(WURST.getGui()));
		setEnabled(false);
	}
	
	public float getOpacity()
	{
		return opacity.getValueF();
	}
	
	public float[] getBgColor()
	{
		float red = bgRed.getValueI() / 255F;
		float green = bgGreen.getValueI() / 255F;
		float blue = bgBlue.getValueI() / 255F;
		return new float[]{red, green, blue};
	}
	
	public float[] getAcColor()
	{
		float red = acRed.getValueI() / 255F;
		float green = acGreen.getValueI() / 255F;
		float blue = acBlue.getValueI() / 255F;
		return new float[]{red, green, blue};
	}
}
