package com.daedalus.ambientevents.wrappers;

import com.daedalus.ambientevents.ParsingUtils;
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

public enum StringType {

    RANDOM("random",RandomStringList::new),
    RAW("raw",RawString::new),
    SEQUENTIAL("sequential",SequentialStringList::new);

    private static final Map<String, StringType> BY_NAME = new HashMap<>();

    public static StringType getOrRaw(String name) {
        name = name.trim().toLowerCase();
        return BY_NAME.getOrDefault(name,RAW);
    }

    public static IString tryAutoParse(@Nullable JsonElement json) throws JsonIOException {
        if(Objects.isNull(json)) return null;
        if(json.isJsonPrimitive()) return RAW.parse(json);
        StringType type = getOrRaw(ParsingUtils.getAsString(json,"type",true));
        return type.parse(ParsingUtils.getNextElement(json,"value",true));
    }

    public static IString tryAutoParse(@Nonnull JsonElement json, String key, boolean shouldThrow) throws JsonIOException {
        return tryAutoParse(ParsingUtils.getNextElement(json,key,shouldThrow));
    }

    public static IString sync(ByteBuf buf) {
        StringType type = getOrRaw(NetworkUtil.readString(buf));
        switch(type) {
            case RANDOM: return new RandomStringList(buf);
            case SEQUENTIAL: return new SequentialStringList(buf);
            default: return new RawString(buf);
        }
    }

    private final String name;
    private final Supplier<IString> constructor;
    StringType(String name, Supplier<IString> constructor) {
        this.name = name;
        this.constructor = constructor;
    }

    public @Nullable IString parse(JsonElement json) {
        IString stringWrapper = this.constructor.get();
        return ParsingUtils.parseElement(json,e -> stringWrapper.parse(json) ? stringWrapper : null);
    }

    @Override
    public String toString() {
        return this.name;
    }

    static {
        for(StringType type : values()) BY_NAME.put(type.name,type);
    }
}
