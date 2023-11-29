package com.daedalus.ambientevents.parsing;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import io.netty.buffer.ByteBuf;

import java.util.Random;

public interface IPrimitiveWrapper<T> {

    boolean parse(JsonElement json) throws JsonIOException;
    T getValue(Random rand);
    void sync(ByteBuf buf);
}
