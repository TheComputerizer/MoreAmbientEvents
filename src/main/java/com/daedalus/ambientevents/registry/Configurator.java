package com.daedalus.ambientevents.registry;

import com.daedalus.ambientevents.AmbientEvents;
import com.daedalus.ambientevents.gui.ConfiguratorGUI;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class Configurator extends Item {

	public Configurator() {
		this.setRegistryName(AmbientEvents.getResource("configurator"));
		this.setCreativeTab(RegistryHandler.AMBIENT_EVENTS_TAB);
	}

	public void initModels() {
		ResourceLocation res = this.getRegistryName();
		if(Objects.nonNull(res))
			ModelLoader.setCustomModelResourceLocation(this,0,new ModelResourceLocation(res,"inventory"));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
		if(worldIn.isRemote)
			Minecraft.getMinecraft().displayGuiScreen(new ConfiguratorGUI(AmbientEvents.clientEventHandler));
		return super.onItemRightClick(worldIn,player,hand);
	}
}
