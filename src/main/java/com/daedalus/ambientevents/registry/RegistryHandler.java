package com.daedalus.ambientevents.registry;

import com.daedalus.ambientevents.AmbientEventsRef;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@EventBusSubscriber(modid = AmbientEventsRef.MODID)
public class RegistryHandler {
    public static final CreativeTabs AMBIENT_EVENTS_TAB = new CreativeTabs(AmbientEventsRef.MODID) {
        @SideOnly(Side.CLIENT)
        public @Nonnull ItemStack createIcon() {
            return new ItemStack(ItemRegistry.CONFIGURATOR);
        }
    };

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(ItemRegistry.CONFIGURATOR);
    }
}
