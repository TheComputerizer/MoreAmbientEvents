package com.daedalus.ambientevents.conditions;

import com.daedalus.ambientevents.ParsingUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.util.NetworkUtil;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MasterCondition implements ICondition {

	public static ICondition newCondition(@Nullable JsonObject json) throws JsonIOException {
		if(Objects.isNull(json)) throw new JsonIOException("Invalid condition");
		String actionType = ParsingUtils.getAsString(json,"type",true);
		if(Objects.nonNull(actionType)) {
			switch(actionType) {
				case "playerpos": return new PlayerPosCondition(json);
				case "alwaystrue": return new AlwaysTrueCondition();
				case "chance": return new ChanceCondition(json);
				case "timeofday": return new TimeOfDayCondition(json);
				case "worldtime": return new WorldTimeCondition(json);
				case "timesincesleep": return new TimeSinceSleepCondition(json);
				case "once": return new OnceCondition();
				case "weather": return new WeatherCondition(json);
				case "canseesky": return new CanSeeSkyCondition();
				default: throw new JsonIOException("Unrecogized condition type: "+actionType);
			}
		}
		return null;
	}

	public static ICondition readCondition(ByteBuf buf) {
		String name = NetworkUtil.readString(buf);
		switch(name) {
			case "playerpos": return new PlayerPosCondition(buf);
			case "alwaystrue": return new AlwaysTrueCondition();
			case "chance": return new ChanceCondition(buf);
			case "timeofday": return new TimeOfDayCondition(buf);
			case "worldtime": return new WorldTimeCondition(buf);
			case "timesincesleep": return new TimeSinceSleepCondition(buf);
			case "once": return new OnceCondition(buf);
			case "weather": return new WeatherCondition(buf);
			case "canseesky": return new CanSeeSkyCondition();
			default: throw new JsonIOException("Unrecogized action type: "+name);
		}
	}

	private final List<ICondition> conditions;

	public MasterCondition(@Nullable JsonArray json) throws JsonIOException {
		this.conditions = new ArrayList<>();
		if(Objects.nonNull(json))
			for(JsonElement element : json)
				this.conditions.add(newCondition(ParsingUtils.getAsObject(element)));
	}

	public MasterCondition(ByteBuf buf) {
		this.conditions = NetworkUtil.readGenericList(buf,MasterCondition::readCondition);
	}

	@Override
	public boolean isMet(EntityPlayer player) {
		for(ICondition condition : this.conditions)
			if(!condition.isMet(player)) return false;
		return true;
	}

	@Override
	public void sync(ByteBuf buf) {
		NetworkUtil.writeGenericList(buf,this.conditions,(buf1,action) -> action.sync(buf1));
	}
}
