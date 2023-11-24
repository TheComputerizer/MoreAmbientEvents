package com.daedalus.ambientevents.conditions;

import com.daedalus.ambientevents.wrappers.NumberType;
import com.daedalus.ambientevents.wrappers.StringType;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import com.daedalus.ambientevents.comparisons.NumericComparison;
import com.daedalus.ambientevents.wrappers.INumber;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Random;

public class TimeOfDayCondition implements ICondition {

	protected NumericComparison comparison;
	protected INumber checkValue;

	public TimeOfDayCondition(JsonObject json) throws JsonIOException {
		this.comparison = new NumericComparison(StringType.tryAutoParse(json,"comparison",true));
		this.checkValue = NumberType.tryAutoParse(json,"value",true);
	}

	public TimeOfDayCondition(ByteBuf buf) {
		this.comparison = new NumericComparison(buf);
		this.checkValue = NumberType.sync(buf);
	}

	@Override
	public boolean isMet(EntityPlayer player) {
		Random rand = player.world.rand;
        return this.comparison.compare(rand,player.world.getWorldTime(),this.checkValue.getValue(rand).doubleValue());
    }

	@Override
	public void sync(ByteBuf buf) {
		this.comparison.sync(buf);
		this.checkValue.sync(buf);
	}
}
