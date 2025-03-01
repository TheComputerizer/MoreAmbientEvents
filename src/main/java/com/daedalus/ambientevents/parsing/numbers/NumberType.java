package com.daedalus.ambientevents.parsing.numbers;

import com.daedalus.ambientevents.parsing.ParsingUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.util.NetworkUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public enum NumberType {

    MAP("map",MapNumber::new),
    RANDOM("random",RandomNumber::new),
    RANDOM_LIST("random_list",RandomNumberList::new),
    RAW("raw",RawNumber::new),
    SEQUENTIAL("sequential",SequentialNumberList::new);

    private static final Map<String,NumberType> BY_NAME = new HashMap<>();

    public static NumberType getOrRaw(String name) {
        name = name.trim().toLowerCase();
        return BY_NAME.getOrDefault(name,RAW);
    }

    public static INumber tryAutoParse(@Nullable JsonElement json) throws JsonIOException {
        if(Objects.isNull(json)) return null;
        if(json.isJsonPrimitive()) return RAW.parse(json);
        NumberType type = getOrRaw(ParsingUtils.getAsString(json,"type",true));
        return type.parse(ParsingUtils.getNextElement(json,"value",true));
    }

    public static INumber tryAutoParse(@Nonnull JsonElement json, String key, boolean shouldThrow) throws JsonIOException {
        return tryAutoParse(ParsingUtils.getNextElement(json,key,shouldThrow));
    }

    public static INumber sync(ByteBuf buf) {
        NumberType type = getOrRaw(NetworkUtil.readString(buf));
        switch(type) {
            case MAP: return new MapNumber(buf);
            case RANDOM: return new RandomNumber(buf);
            case RANDOM_LIST: return new RandomNumberList(buf);
            case SEQUENTIAL: return new SequentialNumberList(buf);
            default: return new RawNumber(buf);
        }
    }

    private final String name;
    private final Supplier<INumber> constructor;
    NumberType(String name, Supplier<INumber> constructor) {
        this.name = name;
        this.constructor = constructor;
    }

    public INumber parse(JsonElement json) {
        INumber numberWrapper = this.constructor.get();
        return ParsingUtils.parseElement(json,e -> numberWrapper.parse(json) ? numberWrapper : null);
    }

    @Override
    public String toString() {
        return this.name;
    }

    static {
        for(NumberType type : values()) BY_NAME.put(type.name,type);
    }
}
