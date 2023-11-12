package com.daedalus.ambientevents;

import com.daedalus.ambientevents.client.AmbientEventsClient;
import com.daedalus.ambientevents.registry.ItemRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = AmbientEventsRef.MODID, name = AmbientEventsRef.NAME, version = AmbientEventsRef.VERSION)
public class AmbientEvents {

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		if(e.getSide().isClient()) {
			AmbientEventsClient.setConfigDir(e.getSuggestedConfigurationFile());
			ItemRegistry.CONFIGURATOR.initModels();
		}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		if(e.getSide().isClient()) AmbientEventsClient.init();
	}

	public static ResourceLocation getResource(String path) {
		return new ResourceLocation(AmbientEventsRef.MODID,path);
	}
}
