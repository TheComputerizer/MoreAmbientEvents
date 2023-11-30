package com.daedalus.ambientevents.registry;

import com.daedalus.ambientevents.AmbientEvents;
import com.daedalus.ambientevents.client.gui.ConfiguratorGUI;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class Configurator extends Item {

	public Configurator() {
		this.setRegistryName(AmbientEvents.getResource("configurator"));
		this.setCreativeTab(RegistryHandler.AMBIENT_EVENTS_TAB);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
		if(worldIn.isRemote)
			Minecraft.getMinecraft().displayGuiScreen(new ConfiguratorGUI(Minecraft.getMinecraft()));
		return super.onItemRightClick(worldIn,player,hand);
	}
}
