/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.mixin;

import org.broulf.admin.AdminClient;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.text.Text;

@Mixin(ShulkerBoxScreen.class)
public abstract class ShulkerBoxScreenMixin
	extends HandledScreen<ShulkerBoxScreenHandler>
	implements ScreenHandlerProvider<ShulkerBoxScreenHandler>
{
	private final int rows = 3;

	private int mode;
	
	public ShulkerBoxScreenMixin(AdminClient wurst,
                                 ShulkerBoxScreenHandler container, PlayerInventory playerInventory,
                                 Text name)
	{
		super(container, playerInventory, name);
	}
	
	@Override
	protected void init()
	{
		super.init();
		
		if(!AdminClient.INSTANCE.isEnabled())
			return;
	}
}
