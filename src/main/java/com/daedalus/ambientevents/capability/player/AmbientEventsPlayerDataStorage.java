package com.daedalus.ambientevents.capability.player;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class AmbientEventsPlayerDataStorage implements Capability.IStorage<IAmbientEventsPlayerData> {

    @Override
    public @Nullable NBTTagCompound writeNBT(Capability<IAmbientEventsPlayerData> cap,
                                             IAmbientEventsPlayerData instance, EnumFacing side) {
        return instance.writeToNBT();
    }

    @Override
    public void readNBT(Capability<IAmbientEventsPlayerData> cap, IAmbientEventsPlayerData instance,
                        EnumFacing side, NBTBase nbt) {
        instance.readFromNBT((NBTTagCompound)nbt);
    }
}
