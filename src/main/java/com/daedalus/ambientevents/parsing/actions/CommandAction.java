package com.daedalus.ambientevents.parsing.actions;

import com.daedalus.ambientevents.AmbientEvents;
import com.daedalus.ambientevents.parsing.ParsingUtils;
import com.daedalus.ambientevents.parsing.strings.RawString;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;
import java.util.Objects;

public class CommandAction extends ServerAction {

    protected boolean commandFeedback;

    public CommandAction(JsonObject json) throws JsonIOException {
        super(json);
        addString(json,"command");
        addString(json,"executeas",new RawString("empty"));
        JsonPrimitive primitive = ParsingUtils.getAsPrimitive(json,"feedback",false);
        this.commandFeedback = Objects.isNull(primitive) || primitive.getAsBoolean();
    }

    public CommandAction(ByteBuf buf) {
        super(buf);
    }

    @Override
    public void execute(EntityPlayer player) {
        List<String> strVals = getStrs(player);
        if(strVals.get(1).matches("empty")) AmbientEvents.executeCommand("/"+strVals.get(0),!this.commandFeedback);
        else AmbientEvents.executeCommand("/execute "+strVals.get(1)+" "+strVals.get(0),!this.commandFeedback);
    }
}
