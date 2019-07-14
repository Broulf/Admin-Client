/*
 * Copyright (C) 2014 - 2018 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks;

import net.wurstclient.clickgui.ClickGuiScreen;
import net.wurstclient.hack.DontSaveState;
import net.wurstclient.hack.Hack;

@DontSaveState
public final class ClickGuiHack extends Hack
{
	// private final SliderSetting opacity = new SliderSetting("Opacity", 0.5,
	// 0.15, 0.85, 0.01, ValueDisplay.PERCENTAGE);
	//
	// private final SliderSetting bgRed = new SliderSetting("BG red",
	// "Background red", 64, 0, 255, 1, ValueDisplay.INTEGER);
	// private final SliderSetting bgGreen = new SliderSetting("BG green",
	// "Background green", 64, 0, 255, 1, ValueDisplay.INTEGER);
	// private final SliderSetting bgBlue = new SliderSetting("BG blue",
	// "Background blue", 64, 0, 255, 1, ValueDisplay.INTEGER);
	//
	// private final SliderSetting acRed = new SliderSetting("AC red",
	// "Accent red", 16, 0, 255, 1, ValueDisplay.INTEGER);
	// private final SliderSetting acGreen = new SliderSetting("AC green",
	// "Accent green", 16, 0, 255, 1, ValueDisplay.INTEGER);
	// private final SliderSetting acBlue = new SliderSetting("AC blue",
	// "Accent blue", 16, 0, 255, 1, ValueDisplay.INTEGER);
	
	public ClickGuiHack()
	{
		super("ClickGUI", "Window-based ClickGUI.");
		// addSetting(opacity);
		// addSetting(bgRed);
		// addSetting(bgGreen);
		// addSetting(bgBlue);
		// addSetting(acRed);
		// addSetting(acGreen);
		// addSetting(acBlue);
	}
	
	@Override
	public void onEnable()
	{
		MC.openScreen(new ClickGuiScreen(WURST.getGui()));
		setEnabled(false);
	}
	
	public float getOpacity()
	{
		// return opacity.getValueF();
		return 0.5F;
	}
	
	public float[] getBgColor()
	{
		return new float[]{0.25F, 0.25F, 0.25F};
		// return new float[]{bgRed.getValueI() / 255F, bgGreen.getValueI() /
		// 255F,
		// bgBlue.getValueI() / 255F};
	}
	
	public float[] getAcColor()
	{
		return new float[]{0.125F, 0.125F, 0.125F};
		// return new float[]{acRed.getValueI() / 255F, acGreen.getValueI() /
		// 255F,
		// acBlue.getValueI() / 255F};
	}
}