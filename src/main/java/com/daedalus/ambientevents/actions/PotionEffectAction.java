package com.daedalus.ambientevents.actions;

import com.daedalus.ambientevents.wrappers.*;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
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

	public PotionEffectAction(JsonObject json) throws JsonIOException {
		super(json);
		this.effect = StringType.tryAutoParse(json,"effect",true);
		this.duration = NumberType.tryAutoParse(json,"duration",true);
		this.amplifier = NumberType.tryAutoParse(json,"amplifier",false);
		if(Objects.isNull(this.amplifier)) this.amplifier = new RawNumber(0d);
	}

	public PotionEffectAction(ByteBuf buf) {
		super(buf);
		this.effect = StringType.sync(buf);
		this.duration = NumberType.sync(buf);
		this.amplifier = NumberType.sync(buf);
	}

	@Override
	public void execute(EntityPlayer player) {
		Random rand = player.world.rand;
		if(this.chance.getValue(rand).doubleValue()>=1d) return;
		ResourceLocation potionRes = new ResourceLocation(this.effect.getValue(rand));
		Potion potion = ForgeRegistries.POTIONS.containsKey(potionRes) ? ForgeRegistries.POTIONS.getValue(potionRes) : null;
		if(Objects.nonNull(potion))
			player.addPotionEffect(new PotionEffect(potion,(int)this.duration.getValue(rand),(int)this.amplifier.getValue(rand)));
	}

	@Override
	public void sync(ByteBuf buf) {
		super.sync(buf);
		this.effect.sync(buf);
		this.duration.sync(buf);
		this.amplifier.sync(buf);
	}
}
