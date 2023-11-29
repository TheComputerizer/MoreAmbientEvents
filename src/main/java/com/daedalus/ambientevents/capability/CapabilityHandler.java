package com.daedalus.ambientevents.capability;

import com.daedalus.ambientevents.AmbientEvents;
import com.daedalus.ambientevents.capability.player.AmbientEventsPlayerDataProvider;
import com.daedalus.ambientevents.capability.player.IAmbientEventsPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Objects;

public class CapabilityHandler {

    public static final ResourceLocation DATA_CAPABILITY = AmbientEvents.getResource("player_data_capability");

    @SuppressWarnings("ConstantConditions")
    public static @Nullable IAmbientEventsPlayerData getPlayerDataCapability(EntityPlayer player) {
        return Objects.nonNull(player) && Objects.nonNull(AmbientEventsPlayerDataProvider.PLAYER_DATA_CAPABILITY) ?
                player.getCapability(AmbientEventsPlayerDataProvider.PLAYER_DATA_CAPABILITY,null) : null;
    }

    public static long getLastSleep(EntityPlayer player) {
        IAmbientEventsPlayerData cap = getPlayerDataCapability(player);
        return Objects.nonNull(cap) ? cap.getLastSleep() : 0L;
    }

    public static void setLastSleep(EntityPlayer player, long time) {
        IAmbientEventsPlayerData cap = getPlayerDataCapability(player);
        if(Objects.nonNull(cap)) cap.setLastSleep(time);
    }

    public static long timeSinceSleep(EntityPlayer player) {
        return player.world.getTotalWorldTime()-getLastSleep(player);
    }
}
