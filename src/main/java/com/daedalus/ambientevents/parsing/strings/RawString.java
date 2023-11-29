package com.daedalus.ambientevents.parsing.strings;

import com.daedalus.ambientevents.parsing.ParsingUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.util.NetworkUtil;

import java.util.Objects;
import java.util.Random;

public class RawString implements IString {

	protected String value;

	public RawString() {}

	public RawString(String value) {
		this.value = value;
	}
	public RawString(ByteBuf buf) {
		this.value = NetworkUtil.readString(buf);
	}

	@Override
	public boolean parse(JsonElement json) throws JsonIOException {
		this.value = ParsingUtils.getAsString(json);
		return Objects.nonNull(this.value);
	}

	@Override
	public String getValue(Random rand) {
		return this.value;
	}

	@Override
	public void sync(ByteBuf buf) {
		NetworkUtil.writeString(buf,"raw");
		NetworkUtil.writeString(buf,this.value);
	}
}
