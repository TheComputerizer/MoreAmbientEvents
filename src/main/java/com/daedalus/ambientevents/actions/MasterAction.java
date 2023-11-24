package com.daedalus.ambientevents.actions;

import com.daedalus.ambientevents.ParsingUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.util.NetworkUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MasterAction implements IAction {

	public static IAction newAction(JsonObject json) throws JsonIOException {
		if(Objects.isNull(json)) throw new JsonIOException("Invalid action");
		String actionType = ParsingUtils.getAsString(json,"type",true);
		if(Objects.nonNull(actionType)) {
			switch(actionType) {
				case "lightning": return new LightningAction(json);
				case "chat": return new ChatAction(json);
				case "potioneffect": return new PotionEffectAction(json);
				case "playsound": return new PlaySoundAction(json);
				default: throw new JsonIOException("Unrecogized action type: "+actionType);
			}
		}
		return null;
	}

	public static IAction readAction(ByteBuf buf) {
		String name = NetworkUtil.readString(buf);
		switch(name) {
			case "lightning": return new LightningAction(buf);
			case "chat": return new ChatAction(buf);
			case "potioneffect": return new PotionEffectAction(buf);
			case "playsound": return new PlaySoundAction(buf);
			default: throw new JsonIOException("Unrecogized action type: "+name);
		}
	}

	private final List<IAction> actions;

	public MasterAction(@Nullable JsonArray json) throws JsonIOException {
		this.actions = new ArrayList<>();
		if(Objects.nonNull(json))
			for(JsonElement element : json)
				this.actions.add(newAction(ParsingUtils.getAsObject(element)));
	}

	public MasterAction(ByteBuf buf) {
		this.actions = NetworkUtil.readGenericList(buf,MasterAction::readAction);
	}

	@Override
	public void execute(EntityPlayer player) {
        for(IAction action : this.actions)
			if(action.canExecute(FMLCommonHandler.instance().getEffectiveSide()))
				action.execute(player);
	}

	@Override
	public boolean canExecute(Side side) {
		return true;
	}

	@Override
	public void sync(ByteBuf buf) {
		NetworkUtil.writeGenericList(buf,this.actions,(buf1,action) -> action.sync(buf1));
	}
}
