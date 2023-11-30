package com.daedalus.ambientevents.parsing;

import com.daedalus.ambientevents.AmbientEvents;
import com.daedalus.ambientevents.parsing.numbers.INumber;
import com.daedalus.ambientevents.parsing.numbers.NumberType;
import com.daedalus.ambientevents.parsing.strings.IString;
import com.daedalus.ambientevents.parsing.strings.StringType;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.util.NetworkUtil;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public abstract class WrapperHolder {

    protected final Map<String,INumber> numberWrappers;
    protected final Map<String,IString> stringWrappers;
    protected WrapperHolder() {
        this.numberWrappers = new HashMap<>();
        this.stringWrappers = new HashMap<>();
    }

    protected WrapperHolder(ByteBuf buf) {
        this.numberWrappers = NetworkUtil.readGenericMap(buf,NetworkUtil::readString,NumberType::sync);
        this.stringWrappers = NetworkUtil.readGenericMap(buf,NetworkUtil::readString,StringType::sync);
    }

    protected void addNumber(JsonObject json, String key) throws JsonIOException {
        addNumber(json,key,null);
    }

    protected void addNumber(JsonObject json, String key, @Nullable INumber fallback) throws JsonIOException {
        try {
            INumber wrapper = NumberType.tryAutoParse(json,key,Objects.isNull(fallback));
            wrapper = Objects.nonNull(wrapper) ? wrapper : fallback;
            if(Objects.nonNull(wrapper)) this.numberWrappers.put(key,wrapper);
        } catch(JsonIOException ex) {
            throw new JsonIOException("Failed to parse number for key "+key,ex);
        }
    }

    protected void addString(JsonObject json, String key) throws JsonIOException {
        addString(json,key,null);
    }

    protected void addString(JsonObject json, String key, @Nullable IString fallback) throws JsonIOException {
        try {
            IString wrapper = StringType.tryAutoParse(json, key, Objects.isNull(fallback));
            wrapper = Objects.nonNull(wrapper) ? wrapper : fallback;
            if (Objects.nonNull(wrapper)) this.stringWrappers.put(key, wrapper);
        } catch (JsonIOException ex) {
            throw new JsonIOException("Failed to parse string for key "+key,ex);
        }
    }

    protected final Number getNum(EntityPlayer player, String id) {
        return getNum(AmbientEvents.entityRand(player),id);
    }

    protected final Number getNum(Random rand, String id) {
        return this.numberWrappers.get(id).getValue(rand);
    }

    protected final String getStr(EntityPlayer player, String id) {
        return getStr(AmbientEvents.entityRand(player),id);
    }

    protected final String getStr(Random rand, String id) {
        return this.stringWrappers.get(id).getValue(rand);
    }

    protected final List<Number> getNums(EntityPlayer player) {
        return getNums(AmbientEvents.entityRand(player));
    }

    protected final List<Number> getNums(Random rand) {
        return this.numberWrappers.values().stream().map(num -> num.getValue(rand)).collect(Collectors.toList());
    }

    protected final List<String> getStrs(EntityPlayer player) {
        return getStrs(AmbientEvents.entityRand(player));
    }

    protected final List<String> getStrs(Random rand) {
        return this.stringWrappers.values().stream().map(num -> num.getValue(rand)).collect(Collectors.toList());
    }

    protected double randRadiusDouble(Random rand, double original, double radius) {
        return original+((rand.nextDouble()*radius*2d)-radius);
    }

    protected int randRadiusInt(Random rand, int original, int radius) {
        return original+(rand.nextInt(radius*2)-radius);
    }

    public void sync(ByteBuf buf) {
        NetworkUtil.writeGenericMap(buf,this.numberWrappers,NetworkUtil::writeString,(buf1,val) -> val.sync(buf1));
        NetworkUtil.writeGenericMap(buf,this.stringWrappers,NetworkUtil::writeString,(buf1,val) -> val.sync(buf1));
    }
}
