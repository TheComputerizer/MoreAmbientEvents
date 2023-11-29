package com.daedalus.ambientevents.parsing.conditions;

import com.daedalus.ambientevents.parsing.ParsingUtils;
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

public class MasterCondition {

	public static Condition newCondition(@Nullable JsonObject json) throws JsonIOException {
		if(Objects.isNull(json)) throw new JsonIOException("Invalid condition");
		String conditionType = ParsingUtils.getAsString(json,"type",true);
		if(Objects.nonNull(conditionType)) {
			switch(conditionType) {
				case "alwaystrue": return new AlwaysTrueCondition();
				case "canseesky": return new CanSeeSkyCondition();
				case "chance": return new ChanceCondition(json);
				case "dimension": return new DimensionCondition(json);
				case "falling": return new FallingCondition(json);
				case "lookingat": return new LookingAtCondition(json);
				case "moon": return new MoonCondition(json);
				case "once": return new OnceCondition();
				case "onfire": return new OnFireCondition(json);
				case "playerpos": return new PlayerPosCondition(json);
				case "season": return new SeasonCondition(json);
				case "submerged": return new SubmergedCondition(json);
				case "timeofday": return new TimeOfDayCondition(json);
				case "timesincesleep": return new TimeSinceSleepCondition(json);
				case "weather": return new WeatherCondition(json);
				case "worldtime": return new WorldTimeCondition(json);
				default: throw new JsonIOException("Unrecogized condition type: "+conditionType);
			}
		}
		return null;
	}

	public static Condition readCondition(ByteBuf buf) {
		String name = NetworkUtil.readString(buf);
		switch(name) {
			case "alwaystrue": return new AlwaysTrueCondition(buf);
			case "canseesky": return new CanSeeSkyCondition(buf);
			case "chance": return new ChanceCondition(buf);
			case "dimension": return new DimensionCondition(buf);
			case "falling": return new FallingCondition(buf);
			case "lookingat": return new LookingAtCondition(buf);
			case "moon": return new MoonCondition(buf);
			case "once": return new OnceCondition(buf);
			case "onfire": return new OnFireCondition(buf);
			case "playerpos": return new PlayerPosCondition(buf);
			case "season": return new SeasonCondition(buf);
			case "submerged": return new SubmergedCondition(buf);
			case "timeofday": return new TimeOfDayCondition(buf);
			case "timesincesleep": return new TimeSinceSleepCondition(buf);
			case "weather": return new WeatherCondition(buf);
			case "worldtime": return new WorldTimeCondition(buf);
			default: throw new JsonIOException("Unrecogized condition type: "+name);
		}
	}

	private final List<Condition> conditions;

	public MasterCondition(@Nullable JsonArray json) throws JsonIOException {
		this.conditions = new ArrayList<>();
		if(Objects.nonNull(json)) {
			for (JsonElement element : json) {
				Condition condition = newCondition(ParsingUtils.getAsObject(element));
				if(Objects.nonNull(condition)) this.conditions.add(condition);
			}
		}
	}

	public MasterCondition(ByteBuf buf) {
		this.conditions = NetworkUtil.readGenericList(buf,MasterCondition::readCondition);
	}

	public boolean isMet(EntityPlayer player) {
		for(Condition condition : this.conditions)
			if(!condition.isMet(player)) return false;
		return true;
	}

	public void sync(ByteBuf buf) {
		NetworkUtil.writeGenericList(buf,this.conditions,(buf1,action) -> action.sync(buf1));
	}
}
