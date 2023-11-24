package com.daedalus.ambientevents;

import com.daedalus.ambientevents.client.AmbientEventsClient;
import com.daedalus.ambientevents.network.PacketSyncConfigData;
import com.daedalus.ambientevents.registry.ItemRegistry;
import mods.thecomputerizer.theimpossiblelibrary.network.NetworkHandler;
import mods.thecomputerizer.theimpossiblelibrary.util.file.FileUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.util.Objects;

@Mod(modid = AmbientEventsRef.MODID, name = AmbientEventsRef.NAME, version = AmbientEventsRef.VERSION)
public class AmbientEvents {

	private static File configDir;

	public AmbientEvents() {
		NetworkHandler.queueClientPacketRegister(PacketSyncConfigData.class);
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		configDir = e.getSuggestedConfigurationFile();
		if(e.getSide().isClient()) ItemRegistry.CONFIGURATOR.initModels();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		if(Objects.isNull(configDir)) {
			AmbientEventsRef.LOGGER.fatal("Could not locate config directory?");
			return;
		}
		if(e.getSide().isClient()) AmbientEventsClient.init();
	}

	public static File getConfigFile(String path, boolean shouldOverride) {
		return FileUtil.generateNestedFile(new File(configDir,AmbientEventsRef.NAME+"/"+path),shouldOverride);
	}

	public static ResourceLocation getResource(String path) {
		return new ResourceLocation(AmbientEventsRef.MODID,path);
	}
}
