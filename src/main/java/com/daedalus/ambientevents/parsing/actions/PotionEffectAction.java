package com.daedalus.ambientevents.parsing.actions;

import com.daedalus.ambientevents.AmbientEvents;
import com.daedalus.ambientevents.parsing.numbers.RawNumber;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class PotionEffectAction extends ServerAction {

	public PotionEffectAction(JsonObject json) throws JsonIOException {
		super(json);
		addString(json,"effect");
		addNumber(json,"duration",new RawNumber(20));
		addNumber(json,"amplifier",new RawNumber(0d));
	}

	public PotionEffectAction(ByteBuf buf) {
		super(buf);
	}

	@Override
	public void execute(EntityPlayer player) {
		Random rand = AmbientEvents.entityRand(player);
		List<Number> numVals = getNums(rand);
		if(numVals.get(0).doubleValue()>=1d) return;
		IForgeRegistry<Potion> potionReg = ForgeRegistries.POTIONS;
		ResourceLocation potionRes = new ResourceLocation(getStr(rand,"effect"));
		Potion potion = potionReg.containsKey(potionRes) ? potionReg.getValue(potionRes) : null;
		if(Objects.nonNull(potion))
			player.addPotionEffect(new PotionEffect(potion,numVals.get(1).intValue(),numVals.get(2).intValue()));
	}
}
