package com.daedalus.ambientevents.parsing.actions;

import com.daedalus.ambientevents.AmbientEvents;
import com.daedalus.ambientevents.parsing.numbers.RawNumber;
import com.daedalus.ambientevents.parsing.strings.RawString;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlaySoundAction extends ClientAction {

	public static final Map<String, SoundEvent> SOUND_MAP = new HashMap<>();

	public PlaySoundAction(JsonObject json) throws JsonIOException {
		super(json);
		addString(json,"sound");
		addString(json,"category",new RawString("ambient"));
		addNumber(json,"volume",new RawNumber(1f));
		addNumber(json,"pitch",new RawNumber(1f));
		addSound();
	}

	private void addSound() throws JsonIOException {
		String soundString = getStr(new Random(),"sound");
		ResourceLocation soundRes = new ResourceLocation(soundString);
		if (ForgeRegistries.SOUND_EVENTS.containsKey(soundRes))
			SOUND_MAP.putIfAbsent(soundString, ForgeRegistries.SOUND_EVENTS.getValue(soundRes));
		else throw new JsonIOException("Sound `" + soundString + "` is not recognized as a valid sound event!");
	}

	public PlaySoundAction(ByteBuf buf) {
		super(buf);
	}

	@Override
	public void execute(EntityPlayer player) {
		Random rand = AmbientEvents.entityRand(player);
		List<Number> numVals = getNums(rand);
		List<String> strVals = getStrs(rand);
		if(numVals.get(0).doubleValue()<1d)
			player.world.playSound(player.posX,player.posY,player.posZ,SOUND_MAP.get(strVals.get(0)),
					SoundCategory.valueOf(strVals.get(1).toUpperCase()),numVals.get(1).floatValue(),
					numVals.get(2).floatValue(),true);
	}
}
