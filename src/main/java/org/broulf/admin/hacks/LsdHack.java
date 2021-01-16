/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.hacks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.broulf.admin.Category;
import org.broulf.admin.hack.DontSaveState;
import org.broulf.admin.hack.Hack;
import org.broulf.admin.mixinterface.IGameRenderer;

@DontSaveState
public final class LsdHack extends Hack
{
	public LsdHack()
	{
		super("LSD", "Causes hallucinations.");
		setCategory(Category.FUN);
	}
	
	@Override
	public void onEnable()
	{
		if(!(MC.getCameraEntity() instanceof PlayerEntity))
		{
			setEnabled(false);
			return;
		}
		
		if(MC.gameRenderer.getShader() != null)
			MC.gameRenderer.disableShader();
		
		((IGameRenderer)MC.gameRenderer)
			.loadWurstShader(new Identifier("shaders/post/wobble.json"));
	}
	
	@Override
	public void onDisable()
	{
		if(MC.gameRenderer.getShader() != null)
			MC.gameRenderer.disableShader();
	}
}
