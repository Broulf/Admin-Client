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

@SearchTags({"auto walk"})
public final class AutoWalkHack extends Hack implements UpdateListener
{
	public AutoWalkHack()
	{
		super("AutoWalk", "Makes you walk automatically.");
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
		
		KeyBinding forwardKey = MC.options.keyForward;
		forwardKey.setPressed(((IKeyBinding)forwardKey).isActallyPressed());
	}
	
	@Override
	public void onUpdate()
	{
		MC.options.keyForward.setPressed(true);
	}
}
