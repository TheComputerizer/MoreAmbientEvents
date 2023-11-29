package com.daedalus.ambientevents.parsing.actions;

import com.daedalus.ambientevents.AmbientEvents;
import com.daedalus.ambientevents.parsing.ParsingUtils;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class ChatAction extends ServerAction {

	protected boolean isLangKey;

	public ChatAction(JsonObject json) throws JsonIOException {
		super(json);
		addString(json,"message");
		JsonPrimitive primitive = ParsingUtils.getAsPrimitive(json,"lang",false);
		this.isLangKey = Objects.nonNull(primitive) && primitive.getAsBoolean();
	}

	public ChatAction(ByteBuf buf) {
		super(buf);
		this.isLangKey = buf.readBoolean();
	}

	@Override
	public void execute(EntityPlayer player) {
		AmbientEvents.chatMsg(player,getStr(player,"message"),this.isLangKey);
	}

	@Override
	public void sync(ByteBuf buf) {
		super.sync(buf);
		buf.writeBoolean(this.isLangKey);
	}
}
