package com.daedalus.ambientevents.conditions;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import net.minecraft.entity.player.EntityPlayer;

public class MasterCondition implements ICondition {

	public static ICondition newCondition(JsonObject args) throws JsonIOException {
		// Factory method for creating new conditions from JSON based config
		if(args.has("type")) {
			String type = args.get("type").getAsString();
			switch(type) {
				case "playerpos": return new PlayerPosCondition(args);
				case "alwaystrue": return new AlwaysTrueCondition();
				case "chance": return new ChanceCondition(args);
				case "timeofday": return new TimeOfDayCondition(args);
				case "worldtime": return new WorldTimeCondition(args);
				case "timesincesleep": return new TimeSinceSleepCondition(args);
				case "once": return new OnceCondition();
				case "weather": return new WeatherCondition(args);
				case "canseesky": return new CanSeeSkyCondition();
				default: throw new JsonIOException("Unrecogized condition type: "+type);
			}
		} else throw new JsonIOException("No type specified");
	}

	private final List<ICondition> conditions;

	public MasterCondition(JsonArray args) throws JsonIOException {
		this.conditions = new ArrayList<>();
		for(JsonElement element : args) this.conditions.add(newCondition((JsonObject)element));
	}

	@Override
	public boolean isMet(EntityPlayer player) {
		for(ICondition condition : this.conditions)
			if(!condition.isMet(player)) return false;
		return true;
	}
}
