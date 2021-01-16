/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.hacks;

import org.broulf.admin.Category;
import org.broulf.admin.Feature;
import org.broulf.admin.SearchTags;
import org.broulf.admin.hack.Hack;
import org.broulf.admin.util.RenderUtils;
import org.lwjgl.opengl.GL11;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import org.broulf.admin.events.RenderListener;

@SearchTags({"prophunt esp"})
public final class ProphuntEspHack extends Hack implements RenderListener
{
	private static final Box FAKE_BLOCK_BOX =
		new Box(-0.5, 0, -0.5, 0.5, 1, 0.5);
	
	public ProphuntEspHack()
	{
		super("ProphuntESP", "Allows you to see fake blocks in Prophunt.\n"
			+ "Made for Mineplex Prophunt. Might not work on other servers.");
		setCategory(Category.RENDER);
	}
	
	@Override
	public void onEnable()
	{
		Feature.EVENTS.add(RenderListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		Feature.EVENTS.remove(RenderListener.class, this);
	}
	
	@Override
	public void onRender(float partialTicks)
	{
		// GL settings
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glLineWidth(2);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);
		
		GL11.glPushMatrix();
		RenderUtils.applyRenderOffset();
		
		// set color
		float alpha = 0.5F + 0.25F * MathHelper
			.sin(System.currentTimeMillis() % 1000 / 500F * (float)Math.PI);
		GL11.glColor4f(1, 0, 0, alpha);
		
		// draw boxes
		for(Entity entity : Feature.MC.world.getEntities())
		{
			if(!(entity instanceof MobEntity))
				continue;
			
			if(!entity.isInvisible())
				continue;
			
			if(Feature.MC.player.squaredDistanceTo(entity) < 0.25)
				continue;
			
			GL11.glPushMatrix();
			GL11.glTranslated(entity.getX(), entity.getY(), entity.getZ());
			
			RenderUtils.drawOutlinedBox(FAKE_BLOCK_BOX);
			RenderUtils.drawSolidBox(FAKE_BLOCK_BOX);
			
			GL11.glPopMatrix();
		}
		
		GL11.glPopMatrix();
		
		// GL resets
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}
}
