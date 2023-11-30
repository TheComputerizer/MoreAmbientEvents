package com.daedalus.ambientevents.parsing.actions;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.relauncher.Side;

public abstract class ClientAction extends Action {

    protected ClientAction(JsonObject json) throws JsonIOException {
        super(json);
    }

    protected ClientAction(ByteBuf buf) {
        super(buf);
    }

    @Override
    public boolean canExecute(Side side) {
        return side.isClient();
    }
}
