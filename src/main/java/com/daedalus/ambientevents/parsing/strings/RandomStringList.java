package com.daedalus.ambientevents.parsing.strings;

import com.daedalus.ambientevents.parsing.ParsingUtils;
import com.daedalus.ambientevents.parsing.WeightedValue;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.util.NetworkUtil;
import net.minecraft.util.WeightedRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class RandomStringList implements IString {

	protected final List<WeightedValue<IString>> values;

	public RandomStringList() {
		this.values = new ArrayList<>();
	}

	public RandomStringList(ByteBuf buf) {
		this.values = NetworkUtil.readGenericList(buf,buf1 -> new WeightedValue<>(StringType.sync(buf1),buf1.readInt()));
	}

	@Override
	public boolean parse(JsonElement json) throws JsonIOException {
		JsonArray textElements = ParsingUtils.getAsArray(json,true);
		if(Objects.isNull(textElements)) return false;
		for(JsonElement element : textElements) {
			IString value = StringType.tryAutoParse(ParsingUtils.getNextElement(element,"value",true));
			if(Objects.nonNull(value)) {
				Number weight = ParsingUtils.getAsNumber(element,"weight",true);
				if(Objects.nonNull(weight)) this.values.add(new WeightedValue<>(value,weight.intValue()));
			}
		}
		return !this.values.isEmpty();
	}

	@Override
	public String getValue(Random rand) {
		return WeightedRandom.getRandomItem(rand,this.values).getValue().getValue(rand);
	}

	@Override
	public void sync(ByteBuf buf) {
		NetworkUtil.writeString(buf,"random");
		NetworkUtil.writeGenericList(buf,this.values,(buf1,val) -> {
			val.getValue().sync(buf1);
			buf1.writeInt(val.itemWeight);
		});
	}
}
