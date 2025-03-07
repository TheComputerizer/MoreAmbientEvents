package com.daedalus.ambientevents.parsing.actions;

import com.daedalus.ambientevents.AmbientEvents;
import com.daedalus.ambientevents.parsing.ParsingUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.util.NetworkUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MasterAction {

	public static Action newAction(@Nullable JsonObject json) {
		String actionType = "";
		try {
			if (Objects.isNull(json)) throw new JsonIOException("Invalid action");
			actionType = ParsingUtils.getAsString(json, "type", true);
			if (Objects.nonNull(actionType)) {
				switch (actionType) {
					case "chat": return new ChatAction(json);
					case "command": return new CommandAction(json);
					case "lightning": return new LightningAction(json);
					case "playsound": return new PlaySoundAction(json);
					case "potioneffect": return new PotionEffectAction(json);
					default: throw new JsonIOException("Unrecogized action type: " + actionType);
				}
			}
		} catch (JsonIOException ex) {
			AmbientEvents.logError("Failed to parse action of type {}!",actionType,ex);
		}
		return null;
	}

	public static Action readAction(ByteBuf buf) {
		String name = "";
		try {
			name = NetworkUtil.readString(buf);
			switch(name) {
				case "chat": return new ChatAction(buf);
				case "command": return new CommandAction(buf);
				case "lightning": return new LightningAction(buf);
				case "playsound": return new PlaySoundAction(buf);
				case "potioneffect": return new PotionEffectAction(buf);
				default: throw new JsonIOException("Unrecogized action type: " + name);
			}
		} catch(JsonIOException ex) {
			AmbientEvents.logError("Failed to parse action of type {}!",name,ex);
		}
		return null;
	}

	private final List<Action> actions;
	private final Side effectiveSide;
	private boolean hasSidedActions = false;

	public MasterAction(@Nullable JsonArray json) throws JsonIOException {
		this.actions = new ArrayList<>();
		this.effectiveSide = AmbientEvents.getSide(true);
		if(Objects.nonNull(json)) {
			for(JsonElement element : json) {
				Action action = newAction(ParsingUtils.getAsObject(element));
				if(Objects.nonNull(action) && checkSide(action)) this.actions.add(action);
			}
		} else AmbientEvents.logWarn("Event was parsed with no actions!");
	}

	public MasterAction(ByteBuf buf) {
		this.actions = NetworkUtil.readGenericList(buf,MasterAction::readAction);
		this.effectiveSide = AmbientEvents.getSide(true);
		for(Action action : this.actions)
			if(checkSide(action)) this.hasSidedActions = true;
	}

	private boolean checkSide(Action action) {
		return action.canExecute(this.effectiveSide);
	}

	public void execute(EntityPlayer player) {
        for(Action action : this.actions)
			if(checkSide(action))
				action.execute(player);
	}

	public boolean hasSidedActions(Side side) {
		return side==this.effectiveSide && this.hasSidedActions;
	}

	public void sync(ByteBuf buf) {
		NetworkUtil.writeGenericList(buf,this.actions,(buf1,action) -> action.sync(buf1));
	}
}
