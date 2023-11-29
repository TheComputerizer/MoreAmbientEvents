package com.daedalus.ambientevents.capability.player;

import net.minecraft.nbt.NBTTagCompound;

public interface IAmbientEventsPlayerData {

    long getLastSleep();
    void setLastSleep(long time);
    NBTTagCompound writeToNBT();
    void readFromNBT(NBTTagCompound tag);
}
