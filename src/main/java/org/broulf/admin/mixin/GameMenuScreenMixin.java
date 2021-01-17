/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.mixin;

import org.broulf.admin.AdminClient;
import org.broulf.admin.options.AdminOptionsScreen;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen
{
	private ButtonWidget wurstOptionsButton;
	
	private GameMenuScreenMixin(AdminClient wurst, Text text_1)
	{
		super(text_1);
	}
	
	@Inject(at = {@At("TAIL")}, method = {"initWidgets()V"})
	private void onInitWidgets(CallbackInfo ci)
	{
		if(!AdminClient.INSTANCE.isEnabled())
			return;
		
		addWurstOptionsButton();
		removeFeedbackAndBugReportButtons();
	}
	
	private void addWurstOptionsButton()
	{
		wurstOptionsButton = new ButtonWidget(width / 2 - 102, height / 4 + 56,
			204, 20, new LiteralText("Admin Client Options"),
			b -> openWurstOptions());
		
		addButton(wurstOptionsButton);
	}
	
	private void openWurstOptions()
	{
		client.openScreen(new AdminOptionsScreen(this));
	}
	
	private void removeFeedbackAndBugReportButtons()
	{
		buttons.removeIf(this::isFeedbackOrBugReportButton);
		children.removeIf(this::isFeedbackOrBugReportButton);
	}
	
	private boolean isFeedbackOrBugReportButton(Element element)
	{
		if(element == null || !(element instanceof AbstractButtonWidget))
			return false;
		
		AbstractButtonWidget button = (AbstractButtonWidget)element;
		String message = button.getMessage().getString();
		
		return message != null
			&& (message.equals(I18n.translate("menu.sendFeedback"))
				|| message.equals(I18n.translate("menu.reportBugs")));
	}
	
	@Inject(at = {@At("TAIL")},
		method = {"render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V"})
	private void onRender(MatrixStack matrixStack, int mouseX, int mouseY,
		float partialTicks, CallbackInfo ci)
	{
		if(!AdminClient.INSTANCE.isEnabled())
			return;
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1, 1, 1, 1);

	}
}
