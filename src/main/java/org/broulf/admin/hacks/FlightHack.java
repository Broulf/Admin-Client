/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.hacks;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.broulf.admin.Category;
import org.broulf.admin.SearchTags;
import org.broulf.admin.events.IsPlayerInWaterListener;
import org.broulf.admin.events.UpdateListener;
import org.broulf.admin.hack.Hack;
import org.broulf.admin.settings.SliderSetting;

@SearchTags({"FlyHack", "fly hack", "flying"})
public final class FlightHack extends Hack
	implements UpdateListener, IsPlayerInWaterListener
{
	public final SliderSetting speed =
		new SliderSetting("Speed", 1, 0.05, 5, 0.05, SliderSetting.ValueDisplay.DECIMAL);
	
	public FlightHack()
	{
		super("Flight",
			"Allows you to you fly.\n\n" + "\u00a7c\u00a7lWARNING:\u00a7r"
				+ " You will take fall damage if you don't use NoFall.");
		setCategory(Category.MOVEMENT);
		addSetting(speed);
	}
	
	@Override
	public void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
		EVENTS.add(IsPlayerInWaterListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
		EVENTS.remove(IsPlayerInWaterListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		ClientPlayerEntity player = MC.player;
		
		player.abilities.flying = false;
		player.flyingSpeed = speed.getValueF();
		
		player.setVelocity(0, 0, 0);
		Vec3d velcity = player.getVelocity();
		
		if(MC.options.keyJump.isPressed())
			player.setVelocity(velcity.add(0, speed.getValue(), 0));
		
		if(MC.options.keySneak.isPressed())
			player.setVelocity(velcity.subtract(0, speed.getValue(), 0));
	}
	
	@Override
	public void onIsPlayerInWater(IsPlayerInWaterEvent event)
	{
		event.setInWater(false);
	}
}
