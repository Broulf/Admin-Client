/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.hacks;

import net.minecraft.client.options.KeyBinding;
import org.broulf.admin.Category;
import org.broulf.admin.SearchTags;
import org.broulf.admin.events.UpdateListener;
import org.broulf.admin.hack.Hack;
import org.broulf.admin.mixinterface.IKeyBinding;
import org.broulf.admin.settings.SliderSetting;

@SearchTags({"miley cyrus", "twerk", "wrecking ball"})
public final class MileyCyrusHack extends Hack implements UpdateListener
{
	private final SliderSetting twerkSpeed = new SliderSetting("Twerk speed",
		"I came in like a wreeecking baaall...", 5, 1, 10, 1,
		SliderSetting.ValueDisplay.INTEGER);
	
	private int timer;
	
	public MileyCyrusHack()
	{
		super("MileyCyrus", "Makes you twerk.");
		setCategory(Category.FUN);
		addSetting(twerkSpeed);
	}
	
	@Override
	public void onEnable()
	{
		timer = 0;
		EVENTS.add(UpdateListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
		
		KeyBinding sneak = MC.options.keySneak;
		sneak.setPressed(((IKeyBinding)sneak).isActallyPressed());
	}
	
	@Override
	public void onUpdate()
	{
		timer++;
		if(timer < 10 - twerkSpeed.getValueI())
			return;
		
		MC.options.keySneak.setPressed(!MC.options.keySneak.isPressed());
		timer = -1;
	}
}
