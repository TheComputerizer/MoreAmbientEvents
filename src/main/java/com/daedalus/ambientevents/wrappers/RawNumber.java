package com.daedalus.ambientevents.wrappers;

import com.daedalus.ambientevents.ParsingUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.util.NetworkUtil;

import java.util.Objects;
import java.util.Random;

public class RawNumber implements INumber {

	protected Number value;

	public RawNumber() {}

	public RawNumber(Number value) {
		this.value = value;
	}

	public RawNumber(ByteBuf buf) {
		String type = NetworkUtil.readString(buf);
		if(type.matches("byte")) this.value = buf.readByte();
		else if(type.matches("float")) this.value = buf.readFloat();
		else if(type.matches("int")) this.value = buf.readInt();
		else if(type.matches("long")) this.value = buf.readLong();
		else if(type.matches("short")) this.value = buf.readShort();
		else this.value = buf.readDouble();
	}

	@Override
	public boolean parse(JsonElement json) throws JsonIOException {
		this.value = ParsingUtils.getAsNumber(json,true);
		return Objects.nonNull(this.value);
	}

	@Override
	public Number getValue(Random rand) {
		return this.value;
	}

	@Override
	public void sync(ByteBuf buf) {
		NetworkUtil.writeString(buf,"raw");
		if(this.value instanceof Byte) {
			NetworkUtil.writeString(buf,"byte");
			buf.writeByte(this.value.byteValue());
		}
		else if(this.value instanceof Float) {
			NetworkUtil.writeString(buf,"float");
			buf.writeFloat(this.value.floatValue());
		}
		else if(this.value instanceof Integer) {
			NetworkUtil.writeString(buf,"int");
			buf.writeFloat(this.value.intValue());
		}
		else if(this.value instanceof Long) {
			NetworkUtil.writeString(buf,"long");
			buf.writeFloat(this.value.longValue());
		}
		else if(this.value instanceof Short) {
			NetworkUtil.writeString(buf,"short");
			buf.writeFloat(this.value.shortValue());
		} else {
			NetworkUtil.writeString(buf,"double");
			buf.writeDouble(this.value.doubleValue());
		}
	}
}
