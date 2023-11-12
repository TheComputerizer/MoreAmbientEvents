package com.daedalus.ambientevents.actions;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import net.minecraft.entity.player.EntityPlayer;

public class MasterAction implements IAction {

	public static IAction newAction(JsonObject args) throws JsonIOException {
		// Factory method for creating new actions from JSON based config
		if(args.has("type")) {
			String actionType = args.get("type").getAsString();
			switch(actionType) {
				case "lightning": return new LightningAction(args);
				case "chat": return new ChatAction(args);
				case "potioneffect": return new PotionEffectAction(args);
				case "playsound": return new PlaySoundAction(args);
				default: throw new JsonIOException("Unrecogized action type: "+actionType);
			}
		} else throw new JsonIOException("No type specified for action!");
	}

	private final List<IAction> actions;

	public MasterAction(JsonArray args) throws JsonIOException {
		this.actions = new ArrayList<>();
		for(JsonElement json : args) this.actions.add(newAction((JsonObject)json));
	}

	@Override
	public void execute(EntityPlayer player) {
        for(IAction action : this.actions) action.execute(player);
	}
}
