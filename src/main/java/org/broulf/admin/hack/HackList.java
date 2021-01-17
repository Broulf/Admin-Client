/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.hack;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import org.broulf.admin.AdminClient;
import org.broulf.admin.event.EventManager;
import org.broulf.admin.events.UpdateListener;
import org.broulf.admin.hacks.*;
import org.broulf.admin.util.json.JsonException;

public final class HackList implements UpdateListener
{
	public final AntiWaterPushHack antiWaterPushHack = new AntiWaterPushHack();
	public final AutoFarmHack autoFarmHack = new AutoFarmHack();
	public final AutoReconnectHack autoReconnectHack = new AutoReconnectHack();
	public final AutoRespawnHack autoRespawnHack = new AutoRespawnHack();
	public final BaseFinderHack baseFinderHack = new BaseFinderHack();
	public final BlinkHack blinkHack = new BlinkHack();
	public final BoatFlyHack boatFlyHack = new BoatFlyHack();
	public final CameraNoClipHack cameraNoClipHack = new CameraNoClipHack();
	public final CaveFinderHack caveFinderHack = new CaveFinderHack();
	public final ChatTranslatorHack chatTranslatorHack = new ChatTranslatorHack();
	public final ChestEspHack chestEspHack = new ChestEspHack();
	public final ClickGuiHack clickGuiHack = new ClickGuiHack();
	public final ExtraElytraHack extraElytraHack = new ExtraElytraHack();
	public final FancyChatHack fancyChatHack = new FancyChatHack();
	public final FastLadderHack fastLadderHack = new FastLadderHack();
	public final FlightHack flightHack = new FlightHack();
	public final ForceOpHack forceOpHack = new ForceOpHack();
	public final FreecamHack freecamHack = new FreecamHack();
	public final FullbrightHack fullbrightHack = new FullbrightHack();
	public final GlideHack glideHack = new GlideHack();
	public final HandNoClipHack handNoClipHack = new HandNoClipHack();
	public final InfiniChatHack infiniChatHack = new InfiniChatHack();
	public final ItemEspHack itemEspHack = new ItemEspHack();
	public final JesusHack jesusHack = new JesusHack();
	public final LiquidsHack liquidsHack = new LiquidsHack();
	public final MassTpaHack massTpaHack = new MassTpaHack();
	public final MobEspHack mobEspHack = new MobEspHack();
	public final MobSpawnEspHack mobSpawnEspHack = new MobSpawnEspHack();
	public final NameProtectHack nameProtectHack = new NameProtectHack();
	public final NameTagsHack nameTagsHack = new NameTagsHack();
	public final NavigatorHack navigatorHack = new NavigatorHack();
	public final NoClipHack noClipHack = new NoClipHack();
	public final NoFallHack noFallHack = new NoFallHack();
	public final NoFireOverlayHack noFireOverlayHack = new NoFireOverlayHack();
	public final NoHurtcamHack noHurtcamHack = new NoHurtcamHack();
	public final NoOverlayHack noOverlayHack = new NoOverlayHack();
	public final NoPumpkinHack noPumpkinHack = new NoPumpkinHack();
	public final NoWeatherHack noWeatherHack = new NoWeatherHack();
	public final NoWebHack noWebHack = new NoWebHack();
	public final OpenWaterEspHack openWaterEspHack = new OpenWaterEspHack();
	public final OverlayHack overlayHack = new OverlayHack();
	public final PanicHack panicHack = new PanicHack();
	public final PlayerEspHack playerEspHack = new PlayerEspHack();
	public final PlayerFinderHack playerFinderHack = new PlayerFinderHack();
	public final RadarHack radarHack = new RadarHack();
	public final RainbowUiHack rainbowUiHack = new RainbowUiHack();
	public final RemoteViewHack remoteViewHack = new RemoteViewHack();
	public final SearchHack searchHack = new SearchHack();
	public final TillauraHack tillauraHack = new TillauraHack();
	public final TimerHack timerHack = new TimerHack();
	public final TiredHack tiredHack = new TiredHack();
	public final TooManyHaxHack tooManyHaxHack = new TooManyHaxHack();
	public final TpAuraHack tpAuraHack = new TpAuraHack();
	public final TrueSightHack trueSightHack = new TrueSightHack();
	
	private final TreeMap<String, Hack> hax =
		new TreeMap<>((o1, o2) -> o1.compareToIgnoreCase(o2));
	
	private final EnabledHacksFile enabledHacksFile;
	private final Path profilesFolder =
		AdminClient.INSTANCE.getAdminFolder().resolve("enabled hacks");
	
	private final EventManager eventManager =
		AdminClient.INSTANCE.getEventManager();
	
	public HackList(Path enabledHacksFile)
	{
		this.enabledHacksFile = new EnabledHacksFile(enabledHacksFile);
		
		try
		{
			for(Field field : HackList.class.getDeclaredFields())
			{
				if(!field.getName().endsWith("Hack"))
					continue;
				
				Hack hack = (Hack)field.get(this);
				hax.put(hack.getName(), hack);
			}
			
		}catch(Exception e)
		{
			String message = "Initializing Wurst hacks";
			CrashReport report = CrashReport.create(e, message);
			throw new CrashException(report);
		}
		
		eventManager.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		enabledHacksFile.load(this);
		eventManager.remove(UpdateListener.class, this);
	}
	
	public void saveEnabledHax()
	{
		enabledHacksFile.save(this);
	}
	
	public Hack getHackByName(String name)
	{
		return hax.get(name);
	}
	
	public Collection<Hack> getAllHax()
	{
		return Collections.unmodifiableCollection(hax.values());
	}
	
	public int countHax()
	{
		return hax.size();
	}
	
	public ArrayList<Path> listProfiles()
	{
		if(!Files.isDirectory(profilesFolder))
			return new ArrayList<>();
		
		try(Stream<Path> files = Files.list(profilesFolder))
		{
			return files.filter(Files::isRegularFile)
				.collect(Collectors.toCollection(() -> new ArrayList<>()));
			
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public void loadProfile(String fileName) throws IOException, JsonException
	{
		enabledHacksFile.loadProfile(this, profilesFolder.resolve(fileName));
	}
	
	public void saveProfile(String fileName) throws IOException, JsonException
	{
		enabledHacksFile.saveProfile(this, profilesFolder.resolve(fileName));
	}
}
