package com.daedalus.ambientevents.actions;

import com.daedalus.ambientevents.wrappers.IString;
import com.daedalus.ambientevents.wrappers.StringType;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

import java.util.Objects;

public class ChatAction extends CommonAction {

	protected IString message;

	public ChatAction(JsonObject json) throws JsonIOException {
		super(json);
		this.message = StringType.tryAutoParse(json,"message",true);
		if(Objects.isNull(this.message)) throw new JsonIOException("Unable to parse message for chat action");
	}

	@Override
	public void execute(EntityPlayer player) {
		player.sendMessage(new TextComponentString(this.message.getValue(player.world.rand)));
	}
}
