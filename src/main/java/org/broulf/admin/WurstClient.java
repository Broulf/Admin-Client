/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.broulf.admin.altmanager.AltManager;
import org.broulf.admin.analytics.WurstAnalytics;
import org.broulf.admin.command.CmdList;
import org.broulf.admin.command.CmdProcessor;
import org.broulf.admin.command.Command;
import org.broulf.admin.keybinds.KeybindList;
import org.broulf.admin.keybinds.KeybindProcessor;
import org.broulf.admin.mixinterface.IMinecraftClient;
import org.broulf.admin.navigator.Navigator;
import org.broulf.admin.other_feature.OtfList;
import org.broulf.admin.other_feature.OtherFeature;
import org.broulf.admin.settings.SettingsFile;
import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Util;
import org.broulf.admin.clickgui.ClickGui;
import org.broulf.admin.event.EventManager;
import org.broulf.admin.events.ChatOutputListener;
import org.broulf.admin.events.GUIRenderListener;
import org.broulf.admin.events.KeyPressListener;
import org.broulf.admin.events.PostMotionListener;
import org.broulf.admin.events.PreMotionListener;
import org.broulf.admin.events.UpdateListener;
import org.broulf.admin.hack.Hack;
import org.broulf.admin.hack.HackList;
import org.broulf.admin.hud.IngameHUD;
import org.broulf.admin.util.json.JsonException;

public enum WurstClient
{
	INSTANCE;
	
	public static final MinecraftClient MC = MinecraftClient.getInstance();
	public static final IMinecraftClient IMC = (IMinecraftClient)MC;
	
	public static final String VERSION = "7.10";
	public static final String MC_VERSION = "1.16.5";
	
	private WurstAnalytics analytics;
	private EventManager eventManager;
	private AltManager altManager;
	private HackList hax;
	private CmdList cmds;
	private OtfList otfs;
	private SettingsFile settingsFile;
	private Path settingsProfileFolder;
	private KeybindList keybinds;
	private ClickGui gui;
	private Navigator navigator;
	private CmdProcessor cmdProcessor;
	private IngameHUD hud;
	private RotationFaker rotationFaker;
	private FriendsList friends;
	
	private boolean enabled = true;
	private static boolean guiInitialized;
	private Path wurstFolder;
	
	private KeyBinding zoomKey;
	
	public void initialize()
	{
		System.out.println("Starting Admin Client...");
		
		wurstFolder = createWurstFolder();
		
		String trackingID = "UA-52838431-5";
		String hostname = "client.wurstclient.net";
		Path analyticsFile = wurstFolder.resolve("analytics.json");
		analytics = new WurstAnalytics(trackingID, hostname, analyticsFile);
		
		eventManager = new EventManager(this);
		
		Path enabledHacksFile = wurstFolder.resolve("enabled-hacks.json");
		hax = new HackList(enabledHacksFile);
		
		cmds = new CmdList();
		
		otfs = new OtfList();
		
		Path settingsFile = wurstFolder.resolve("settings.json");
		settingsProfileFolder = wurstFolder.resolve("settings");
		this.settingsFile = new SettingsFile(settingsFile, hax, cmds, otfs);
		this.settingsFile.load();
		hax.tooManyHaxHack.loadBlockedHacksFile();
		
		Path keybindsFile = wurstFolder.resolve("keybinds.json");
		keybinds = new KeybindList(keybindsFile);
		
		Path guiFile = wurstFolder.resolve("windows.json");
		gui = new ClickGui(guiFile);
		
		Path preferencesFile = wurstFolder.resolve("preferences.json");
		navigator = new Navigator(preferencesFile, hax);
		
		Path friendsFile = wurstFolder.resolve("friends.json");
		friends = new FriendsList(friendsFile);
		friends.load();
		
		cmdProcessor = new CmdProcessor(cmds);
		eventManager.add(ChatOutputListener.class, cmdProcessor);
		
		KeybindProcessor keybindProcessor =
			new KeybindProcessor(hax, keybinds, cmdProcessor);
		eventManager.add(KeyPressListener.class, keybindProcessor);
		
		hud = new IngameHUD();
		eventManager.add(GUIRenderListener.class, hud);
		
		rotationFaker = new RotationFaker();
		eventManager.add(PreMotionListener.class, rotationFaker);
		eventManager.add(PostMotionListener.class, rotationFaker);

		Path altsFile = wurstFolder.resolve("alts.encrypted_json");
		Path encFolder = createEncryptionFolder();
		altManager = new AltManager(altsFile, encFolder);
		
		zoomKey = new KeyBinding("key.wurst.zoom", InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_V, "Zoom");
		KeyBindingHelper.registerKeyBinding(zoomKey);
		
		analytics.trackPageView("/mc" + MC_VERSION + "/v" + VERSION,
			"Wurst " + VERSION + " MC" + MC_VERSION);
	}
	
	private Path createWurstFolder()
	{
		Path dotMinecraftFolder = MC.runDirectory.toPath().normalize();
		Path wurstFolder = dotMinecraftFolder.resolve("wurst");
		
		try
		{
			Files.createDirectories(wurstFolder);
			
		}catch(IOException e)
		{
			throw new RuntimeException(
				"Couldn't create .minecraft/wurst folder.", e);
		}
		
		return wurstFolder;
	}
	
	private Path createEncryptionFolder()
	{
		Path encFolder =
			Paths.get(System.getProperty("user.home"), ".Wurst encryption")
				.normalize();
		
		try
		{
			Files.createDirectories(encFolder);
			if(Util.getOperatingSystem() == Util.OperatingSystem.WINDOWS)
				Files.setAttribute(encFolder, "dos:hidden", true);
			
			Path readme = encFolder.resolve("READ ME I AM VERY IMPORTANT.txt");
			String readmeText = "DO NOT SHARE THESE FILES WITH ANYONE!\r\n"
				+ "They are encryption keys that protect your alt list file from being read by someone else.\r\n"
				+ "If someone is asking you to send these files, they are 100% trying to scam you.\r\n"
				+ "\r\n"
				+ "DO NOT EDIT, RENAME OR DELETE THESE FILES! (unless you know what you're doing)\r\n"
				+ "If you do, Wurst's Alt Manager can no longer read your alt list and will replace it with a blank one.\r\n"
				+ "In other words, YOUR ALT LIST WILL BE DELETED.";
			Files.write(readme, readmeText.getBytes("UTF-8"),
				StandardOpenOption.CREATE);
			
		}catch(IOException e)
		{
			throw new RuntimeException(
				"Couldn't create '.Wurst encryption' folder.", e);
		}
		
		return encFolder;
	}
	
	public WurstAnalytics getAnalytics()
	{
		return analytics;
	}
	
	public EventManager getEventManager()
	{
		return eventManager;
	}
	
	public void saveSettings()
	{
		settingsFile.save();
	}
	
	public ArrayList<Path> listSettingsProfiles()
	{
		if(!Files.isDirectory(settingsProfileFolder))
			return new ArrayList<>();
		
		try(Stream<Path> files = Files.list(settingsProfileFolder))
		{
			return files.filter(Files::isRegularFile)
				.collect(Collectors.toCollection(() -> new ArrayList<>()));
			
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public void loadSettingsProfile(String fileName)
		throws IOException, JsonException
	{
		settingsFile.loadProfile(settingsProfileFolder.resolve(fileName));
	}
	
	public void saveSettingsProfile(String fileName)
		throws IOException, JsonException
	{
		settingsFile.saveProfile(settingsProfileFolder.resolve(fileName));
	}
	
	public HackList getHax()
	{
		return hax;
	}
	
	public CmdList getCmds()
	{
		return cmds;
	}
	
	public OtfList getOtfs()
	{
		return otfs;
	}
	
	public Feature getFeatureByName(String name)
	{
		Hack hack = getHax().getHackByName(name);
		if(hack != null)
			return hack;
		
		Command cmd = getCmds().getCmdByName(name.substring(1));
		if(cmd != null)
			return cmd;
		
		OtherFeature otf = getOtfs().getOtfByName(name);
		if(otf != null)
			return otf;
		
		return null;
	}
	
	public KeybindList getKeybinds()
	{
		return keybinds;
	}
	
	public ClickGui getGui()
	{
		if(!guiInitialized)
		{
			guiInitialized = true;
			gui.init();
		}
		
		return gui;
	}
	
	public Navigator getNavigator()
	{
		return navigator;
	}
	
	public CmdProcessor getCmdProcessor()
	{
		return cmdProcessor;
	}
	
	public IngameHUD getHud()
	{
		return hud;
	}
	
	public RotationFaker getRotationFaker()
	{
		return rotationFaker;
	}
	
	public FriendsList getFriends()
	{
		return friends;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;

		if(!enabled)
		{
			hax.panicHack.setEnabled(true);
			hax.panicHack.onUpdate();
		}
	}
	
	public Path getWurstFolder()
	{
		return wurstFolder;
	}
	
	public KeyBinding getZoomKey()
	{
		return zoomKey;
	}
	
	public AltManager getAltManager()
	{
		return altManager;
	}
}
