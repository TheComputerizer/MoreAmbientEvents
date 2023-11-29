package com.daedalus.ambientevents.network;

import com.daedalus.ambientevents.AmbientEventsRef;
import com.daedalus.ambientevents.client.ClientEventManager;
import com.daedalus.ambientevents.parsing.ParsingUtils;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.network.MessageImpl;
import mods.thecomputerizer.theimpossiblelibrary.util.NetworkUtil;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSyncConfigData extends MessageImpl {

    private JsonObject json;

    public PacketSyncConfigData() {}

    public PacketSyncConfigData(JsonObject json) {
        this.json = json;
    }

    @Override
    public IMessage handle(MessageContext ctx) {
        ClientEventManager.init(this.json);
        return null;
    }

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.json = ParsingUtils.getAsObject(AmbientEventsRef.PARSER.parse(NetworkUtil.readString(buf)));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkUtil.writeString(buf,this.json.toString());
    }
}
