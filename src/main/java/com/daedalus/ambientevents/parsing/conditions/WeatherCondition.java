package com.daedalus.ambientevents.parsing.conditions;

import com.google.gson.JsonObject;
import com.google.gson.JsonIOException;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class WeatherCondition extends Condition {

	public WeatherCondition(JsonObject json) throws JsonIOException {
		super();
		addString(json,"condition");
	}

	public WeatherCondition(ByteBuf buf) {
		super(buf);
	}

	@Override
	public boolean isMet(EntityPlayer player) {
		switch(getStr(player,"condition")) {
		case "raining": return player.world.getWorldInfo().isRaining();
		case "thundering": return player.world.getWorldInfo().isThundering();
		case "clear": return !(player.world.getWorldInfo().isRaining() || player.world.getWorldInfo().isThundering());
		default: return false;
		}
	}
}
