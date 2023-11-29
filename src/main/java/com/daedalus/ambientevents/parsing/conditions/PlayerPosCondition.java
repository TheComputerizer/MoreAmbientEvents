package com.daedalus.ambientevents.parsing.conditions;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerPosCondition extends ComparisonCondition {

	public PlayerPosCondition(JsonObject json) throws JsonIOException {
		super(json);
		addString(json,"pos");
	}

	public PlayerPosCondition(ByteBuf buf) {
		super(buf);
	}

	@Override
	public boolean isMet(EntityPlayer player) {
		double posVal;
		switch(getStr(player,"pos")) {
			case "x":
				posVal = player.posX;
				break;
			case "y":
				posVal = player.posY;
				break;
			case "z":
				posVal = player.posZ;
				break;
			default: return false;
		}
		return compare(player,posVal);
	}
}
