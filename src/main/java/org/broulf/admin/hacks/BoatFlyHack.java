/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.hacks;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.broulf.admin.Category;
import org.broulf.admin.SearchTags;
import org.broulf.admin.events.UpdateListener;
import org.broulf.admin.hack.Hack;

@SearchTags({"BoatFlight", "boat fly", "boat flight"})
public final class BoatFlyHack extends Hack implements UpdateListener
{
	public BoatFlyHack()
	{
		super("BoatFly", "Allows you to fly with boats");
		setCategory(Category.MOVEMENT);
	}
	
	@Override
	public void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		// check if riding
		if(!MC.player.hasVehicle())
			return;
		
		// fly
		Entity vehicle = MC.player.getVehicle();
		Vec3d velocity = vehicle.getVelocity();
		double motionY = MC.options.keyJump.isPressed() ? 0.3 : 0;
		vehicle.setVelocity(new Vec3d(velocity.x, motionY, velocity.z));
	}
}
