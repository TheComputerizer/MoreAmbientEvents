package com.daedalus.ambientevents.network;

import com.daedalus.ambientevents.GenericEvent;
import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.network.MessageImpl;
import mods.thecomputerizer.theimpossiblelibrary.util.NetworkUtil;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class PacketSyncConfigData extends MessageImpl {

    private List<GenericEvent> events;

    public PacketSyncConfigData() {}

    public PacketSyncConfigData(List<GenericEvent> events) {
        this.events = events;
    }

    @Override
    public IMessage handle(MessageContext ctx) {
        return null;
    }

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.events = NetworkUtil.readGenericList(buf,GenericEvent::new);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkUtil.writeGenericList(buf,this.events,(buf1,event) -> event.sync(buf1));
    }
}
