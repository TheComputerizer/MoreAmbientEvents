package com.daedalus.ambientevents.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.daedalus.ambientevents.ParsingUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;

public class MasterAction implements IAction {

	public static IAction newAction(JsonObject json) throws JsonIOException {
		if(Objects.isNull(json)) throw new JsonIOException("Invalid action");
		String actionType = ParsingUtils.getAsString(json,"type",true);
		if(Objects.nonNull(actionType)) {
			switch(actionType) {
				case "lightning": return new LightningAction(json);
				case "chat": return new ChatAction(json);
				case "potioneffect": return new PotionEffectAction(json);
				case "playsound": return new PlaySoundAction(json);
				default: throw new JsonIOException("Unrecogized action type: "+actionType);
			}
		}
		return null;
	}

	private final List<IAction> actions;

	public MasterAction(@Nullable JsonArray json) throws JsonIOException {
		this.actions = new ArrayList<>();
		if(Objects.nonNull(json))
			for(JsonElement element : json)
				this.actions.add(newAction(ParsingUtils.getAsObject(element)));
	}

	@Override
	public void execute(EntityPlayer player) {
        for(IAction action : this.actions) action.execute(player);
	}
}
