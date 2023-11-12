package com.daedalus.ambientevents.actions;

import com.daedalus.ambientevents.wrappers.INumber;
import com.daedalus.ambientevents.wrappers.IString;
import com.daedalus.ambientevents.wrappers.Wrapper;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

import java.util.*;

public class PlaySoundAction extends CommonAction {

	protected IString sound;
	protected IString category;
	protected INumber volume;
	protected INumber pitch;
	public static Map<String, SoundEvent> registry = null;

	public static void InitRegistry() {
		Iterator<SoundEvent> soundsIT = SoundEvent.REGISTRY.iterator();
		registry = new HashMap<>();
		while(soundsIT.hasNext()) {
			SoundEvent event = soundsIT.next();
			registry.put(event.getRegistryName().getPath(), event);
		}
	}

	public PlaySoundAction(JsonObject args) throws JsonIOException {
		super(args);
		if(args.has("sound")) this.sound = Wrapper.newString(args.get("sound"));
		else throw new JsonIOException("No sound specified");
		if(args.has("category")) this.category = Wrapper.newString(args.get("category"));
		else this.category = Wrapper.newString("ambient");
		if(args.has("volume")) this.volume = Wrapper.newNumber(args.get("volume"));
		else this.volume = Wrapper.newNumber(1);
		if(args.has("pitch")) this.pitch = Wrapper.newNumber(args.get("pitch"));
		else this.pitch = Wrapper.newNumber(1);
		if(Objects.isNull(registry)) InitRegistry();
	}

	@Override
	public void execute(EntityPlayer player) {
		Random rand = player.world.rand;
		if(this.chance.getValue(rand)<1) {
			player.world.playSound(player.posX,player.posY,player.posZ,registry.get(this.sound.getValue()),
					SoundCategory.valueOf(this.category.getValue().toUpperCase()),(float) this.volume.getValue(rand),
					(float)this.pitch.getValue(rand),true);
		}
	}
}
