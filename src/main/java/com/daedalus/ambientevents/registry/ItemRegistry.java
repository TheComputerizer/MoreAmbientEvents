package com.daedalus.ambientevents.registry;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

public class ItemRegistry {

    public static final Configurator CONFIGURATOR = new Configurator();

    @SideOnly(Side.CLIENT)
    public static void initItemModels() {
        initGenericModel(CONFIGURATOR);
    }

    @SideOnly(Side.CLIENT)
    public static void initGenericModel(Item item) {
        ResourceLocation res = item.getRegistryName();
        if(Objects.nonNull(res)) {
            ModelResourceLocation modelRes = new ModelResourceLocation(res,"inventory");
            ModelLoader.setCustomModelResourceLocation(item,0,modelRes);
        }
    }
}
