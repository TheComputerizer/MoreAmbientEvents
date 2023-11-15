package com.daedalus.ambientevents.actions;

import com.daedalus.ambientevents.wrappers.*;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.*;

public class PlaySoundAction extends CommonAction {

	public static final Map<String,SoundEvent> SOUND_MAP = new HashMap<>();
	protected IString sound;
	protected IString category;
	protected INumber volume;
	protected INumber pitch;

	public PlaySoundAction(JsonObject json) throws JsonIOException {
		super(json);
		this.sound = StringType.tryAutoParse(json,"sound",true);
		this.category = StringType.tryAutoParse(json,"category",false);
		if(Objects.isNull(this.category)) this.category = new RawString("ambient");
		this.volume = NumberType.tryAutoParse(json,"volume",false);
		if(Objects.isNull(this.volume)) this.volume = new RawNumber(1f);
		this.pitch = NumberType.tryAutoParse(json,"pitch",false);
		if(Objects.isNull(this.pitch)) this.pitch = new RawNumber(1f);
		addSound(new Random());
	}

	private void addSound(Random rand) throws JsonIOException {
		String soundString = this.sound.getValue(rand);
		ResourceLocation soundRes = new ResourceLocation(soundString);
		if(ForgeRegistries.SOUND_EVENTS.containsKey(soundRes))
			SOUND_MAP.putIfAbsent(soundString,ForgeRegistries.SOUND_EVENTS.getValue(soundRes));
		else throw new JsonIOException("Sound `"+soundString+"` is not recognized as a valid sound event!");
	}

	@Override
	public void execute(EntityPlayer player) {
		Random rand = player.world.rand;
		if(this.chance.getValue(rand).doubleValue()<1d) {
			player.world.playSound(player.posX,player.posY,player.posZ,SOUND_MAP.get(this.sound.getValue(rand)),
					SoundCategory.valueOf(this.category.getValue(rand).toUpperCase()),this.volume.getValue(rand).floatValue(),
					this.pitch.getValue(rand).floatValue(),true);
		}
	}
}
