/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.options;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.broulf.admin.AdminClient;
import org.broulf.admin.analytics.WurstAnalytics;
import org.broulf.admin.commands.FriendsCmd;
import org.broulf.admin.other_features.VanillaSpoofOtf;
import org.broulf.admin.settings.CheckboxSetting;

public class AdminOptionsScreen extends Screen
{
	private Screen prevScreen;
	
	public AdminOptionsScreen(Screen prevScreen)
	{
		super(new LiteralText(""));
		this.prevScreen = prevScreen;
	}
	
	@Override
	public void init()
	{
		addButton(new ButtonWidget(width / 2 - 100, height / 4 + 144 - 16, 200,
			20, new LiteralText("Back"), b -> client.openScreen(prevScreen)));
		
		addSettingButtons();
		addManagerButtons();
	}
	
	private void addSettingButtons()
	{
		AdminClient wurst = AdminClient.INSTANCE;
		FriendsCmd friendsCmd = wurst.getCmds().friendsCmd;
		CheckboxSetting middleClickFriends = friendsCmd.getMiddleClickFriends();
		WurstAnalytics analytics = wurst.getAnalytics();
		VanillaSpoofOtf vanillaSpoofOtf = wurst.getOtfs().vanillaSpoofOtf;
		
		new WurstOptionsButton(-50, 24,
			() -> "Click Friends: "
				+ (middleClickFriends.isChecked() ? "ON" : "OFF"),
			middleClickFriends.getDescription(), b -> middleClickFriends
				.setChecked(!middleClickFriends.isChecked()));
		
		new WurstOptionsButton(-50, 48,
			() -> "Count Users: " + (analytics.isEnabled() ? "ON" : "OFF"),
			"Counts how many people are using Wurst\n"
				+ "and which versions are the most popular.\n"
				+ "We use this data to decide when to stop\n"
				+ "supporting old Minecraft versions.\n\n"
				+ "We use a random ID to tell users apart\n"
				+ "so that this data can never be linked to\n"
				+ "your Minecraft account. The random ID is\n"
				+ "changed every 3 days to make extra sure\n"
				+ "that you remain anonymous.",
			b -> analytics.setEnabled(!analytics.isEnabled()));
		
		new WurstOptionsButton(-50, 72,
			() -> "Spoof Vanilla: "
				+ (vanillaSpoofOtf.isEnabled() ? "ON" : "OFF"),
			vanillaSpoofOtf.getDescription(),
			b -> vanillaSpoofOtf.doPrimaryAction());
	}
	
	private void addManagerButtons()
	{
		new WurstOptionsButton(-50, 96, () -> "Zoom",
			"The Zoom Manager allows you to\n"
				+ "change the zoom key, how far it\n"
				+ "will zoom in and more.",
			b -> client.openScreen(new ZoomManagerScreen(this)));
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY,
		float partialTicks)
	{
		renderBackground(matrixStack);
		renderTitles(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		renderButtonTooltip(matrixStack, mouseX, mouseY);
	}
	
	private void renderTitles(MatrixStack matrixStack)
	{
		TextRenderer tr = client.textRenderer;
		int middleX = width / 2;
		int y1 = 40;
		int y2 = height / 4 + 24 - 28;
		
		drawCenteredString(matrixStack, tr, "Admin Options", middleX, y2 - 25,
			0xffffff);
		
		drawCenteredString(matrixStack, tr, "Settings", middleX, y2,
			0xcccccc);
	}
	
	private void renderButtonTooltip(MatrixStack matrixStack, int mouseX,
		int mouseY)
	{
		for(AbstractButtonWidget button : buttons)
		{
			if(!button.isHovered() || !(button instanceof WurstOptionsButton))
				continue;
			
			WurstOptionsButton woButton = (WurstOptionsButton)button;
			if(woButton.tooltip.isEmpty())
				continue;
			
			renderTooltip(matrixStack, woButton.tooltip, mouseX, mouseY);
			break;
		}
	}
	
	private final class WurstOptionsButton extends ButtonWidget
	{
		private final Supplier<String> messageSupplier;
		private final List<Text> tooltip;
		
		public WurstOptionsButton(int xOffset, int yOffset,
			Supplier<String> messageSupplier, String tooltip,
			PressAction pressAction)
		{
			super(AdminOptionsScreen.this.width / 2 + xOffset,
				AdminOptionsScreen.this.height / 4 - 16 + yOffset, 100, 20,
				new LiteralText(messageSupplier.get()), pressAction);
			
			this.messageSupplier = messageSupplier;
			
			if(tooltip.isEmpty())
				this.tooltip = Arrays.asList(new LiteralText[0]);
			else
			{
				String[] lines = tooltip.split("\n");
				
				LiteralText[] lines2 = new LiteralText[lines.length];
				for(int i = 0; i < lines.length; i++)
					lines2[i] = new LiteralText(lines[i]);
				
				this.tooltip = Arrays.asList(lines2);
			}
			
			addButton(this);
		}
		
		@Override
		public void onPress()
		{
			super.onPress();
			setMessage(new LiteralText(messageSupplier.get()));
		}
	}
}
