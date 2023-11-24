package com.daedalus.ambientevents.conditions;

import com.daedalus.ambientevents.comparisons.NumericComparison;
import com.daedalus.ambientevents.wrappers.INumber;
import com.daedalus.ambientevents.wrappers.IString;
import com.daedalus.ambientevents.wrappers.NumberType;
import com.daedalus.ambientevents.wrappers.StringType;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Random;

public class PlayerPosCondition implements ICondition {

	protected IString pos;
	protected NumericComparison comparison;
	protected INumber compareValue;

	public PlayerPosCondition(JsonObject json) throws JsonIOException {
		this.pos = StringType.tryAutoParse(json,"pos",true);
		this.comparison = new NumericComparison(StringType.tryAutoParse(json,"comparison",true));
		this.compareValue = NumberType.tryAutoParse(json,"compareValue",true);
	}

	public PlayerPosCondition(ByteBuf buf) {
		this.pos = StringType.sync(buf);
		this.comparison = new NumericComparison(buf);
		this.compareValue = NumberType.sync(buf);
	}

	@Override
	public boolean isMet(EntityPlayer player) {
		double playerValue;
		Random rand = player.world.rand;
		switch(this.pos.getValue(rand)) {
		case "x":
			playerValue = player.posX;
			break;
		case "y":
			playerValue = player.posY;
			break;
		case "z":
			playerValue = player.posZ;
			break;
		default: return false;
		}
		return this.comparison.compare(rand,playerValue,this.compareValue.getValue(rand).doubleValue());
	}

	@Override
	public void sync(ByteBuf buf) {
		this.pos.sync(buf);
		this.comparison.sync(buf);
		this.compareValue.sync(buf);
	}
}
