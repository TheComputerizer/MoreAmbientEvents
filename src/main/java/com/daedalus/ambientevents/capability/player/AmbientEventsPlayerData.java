package com.daedalus.ambientevents.capability.player;

import com.daedalus.ambientevents.AmbientEvents;
import net.minecraft.nbt.NBTTagCompound;

public class AmbientEventsPlayerData implements IAmbientEventsPlayerData {

    private long lastSleep;

    public AmbientEventsPlayerData() {
        AmbientEvents.logDebug("Initializing player data capability");
    }

    @Override
    public long getLastSleep() {
        return this.lastSleep;
    }

    @Override
    public void setLastSleep(long time) {
        this.lastSleep = time;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setLong("lastSleepTime",this.lastSleep);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        this.lastSleep = tag.getLong("lastSleepTime");
    }
}
