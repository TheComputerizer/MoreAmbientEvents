package com.daedalus.ambientevents.actions;

import com.daedalus.ambientevents.wrappers.INumber;
import com.daedalus.ambientevents.wrappers.IString;
import com.daedalus.ambientevents.wrappers.Wrapper;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Objects;
import java.util.Random;

public class PotionEffectAction extends CommonAction {

	protected IString effect;
	protected INumber duration;
	protected INumber amplifier;

	public PotionEffectAction(JsonObject args) throws JsonIOException {
		super(args);
		if(args.has("effect")) this.effect = Wrapper.newString(args.get("effect"));
		else throw new JsonIOException("No potion ID given");
		if(args.has("duration")) this.duration = Wrapper.newNumber(args.get("duration"));
		else throw new JsonIOException("No potion duration given");
		if(args.has("amplifier")) this.amplifier = Wrapper.newNumber(args.get("amplifier"));
		else this.amplifier = Wrapper.newNumber(0);
	}

	@Override
	public void execute(EntityPlayer player) {
		Random rand = player.world.rand;
		if(this.chance.getValue(rand)>=1) return;
		ResourceLocation potionRes = new ResourceLocation(this.effect.getValue());
		Potion potion = ForgeRegistries.POTIONS.containsKey(potionRes) ? ForgeRegistries.POTIONS.getValue(potionRes) : null;
		if(Objects.nonNull(potion))
			player.addPotionEffect(new PotionEffect(potion,(int)this.duration.getValue(rand),(int)this.amplifier.getValue(rand)));
	}
}
