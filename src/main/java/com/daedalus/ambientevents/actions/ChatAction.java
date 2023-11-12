package com.daedalus.ambientevents.actions;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import com.daedalus.ambientevents.wrappers.IString;
import com.daedalus.ambientevents.wrappers.Wrapper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class ChatAction extends CommonAction {

	protected IString message;

	public ChatAction(JsonObject args) throws JsonIOException {
		super(args);
		if(args.has("message")) this.message = Wrapper.newString(args.get("message"));
		else throw new JsonIOException("No message specified");
	}

	@Override
	public void execute(EntityPlayer player) {
		player.sendMessage(new TextComponentString(this.message.getValue()));
	}
}
