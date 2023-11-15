package com.daedalus.ambientevents.conditions;

import com.daedalus.ambientevents.wrappers.StringType;
import com.google.gson.JsonIOException;

import com.daedalus.ambientevents.wrappers.IString;

import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayer;

public class WeatherCondition implements ICondition {

	protected IString condition;

	public WeatherCondition(JsonObject json) throws JsonIOException {
		this.condition = StringType.tryAutoParse(json,"condition",true);
	}

	@Override
	public boolean isMet(EntityPlayer player) {
		switch(this.condition.getValue(player.world.rand)) {
		case "raining": return player.world.getWorldInfo().isRaining();
		case "thundering": return player.world.getWorldInfo().isThundering();
		case "clear": return !(player.world.getWorldInfo().isRaining() || player.world.getWorldInfo().isThundering());
		default: return false;
		}
	}
}
