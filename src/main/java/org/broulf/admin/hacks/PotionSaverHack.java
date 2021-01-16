/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.hacks;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.broulf.admin.Category;
import org.broulf.admin.SearchTags;
import org.broulf.admin.events.PacketOutputListener;
import org.broulf.admin.hack.Hack;
import org.broulf.admin.Feature;

@SearchTags({"potion saver"})
public final class PotionSaverHack extends Hack implements PacketOutputListener
{
	public PotionSaverHack()
	{
		super("PotionSaver",
			"Freezes all potion effects while you are standing still.");
		setCategory(Category.OTHER);
	}
	
	@Override
	protected void onEnable()
	{
		Feature.EVENTS.add(PacketOutputListener.class, this);
	}
	
	@Override
	protected void onDisable()
	{
		Feature.EVENTS.remove(PacketOutputListener.class, this);
	}
	
	@Override
	public void onSentPacket(PacketOutputEvent event)
	{
		if(!isFrozen())
			return;
		
		if(event.getPacket() instanceof PlayerMoveC2SPacket)
			event.cancel();
	}
	
	public boolean isFrozen()
	{
		return isEnabled() && Feature.MC.player != null
			&& !Feature.MC.player.getActiveStatusEffects().isEmpty()
			&& Feature.MC.player.getVelocity().x == 0 && Feature.MC.player.getVelocity().z == 0;
	}
}
