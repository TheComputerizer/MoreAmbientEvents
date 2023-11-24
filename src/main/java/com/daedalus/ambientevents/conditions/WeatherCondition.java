package com.daedalus.ambientevents.conditions;

import com.daedalus.ambientevents.wrappers.StringType;
import com.google.gson.JsonIOException;

import com.daedalus.ambientevents.wrappers.IString;

import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class WeatherCondition implements ICondition {

	protected IString condition;

	public WeatherCondition(JsonObject json) throws JsonIOException {
		this.condition = StringType.tryAutoParse(json,"condition",true);
	}

	public WeatherCondition(ByteBuf buf) {
		this.condition = StringType.sync(buf);
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

	@Override
	public void sync(ByteBuf buf) {
		this.condition.sync(buf);
	}
}
