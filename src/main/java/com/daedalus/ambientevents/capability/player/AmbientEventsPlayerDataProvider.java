package com.daedalus.ambientevents.capability.player;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("ConstantConditions")
public class AmbientEventsPlayerDataProvider implements ICapabilitySerializable<NBTTagCompound> {


    @CapabilityInject(IAmbientEventsPlayerData.class)
    public static final Capability<IAmbientEventsPlayerData> PLAYER_DATA_CAPABILITY = null;
    private final IAmbientEventsPlayerData impl = PLAYER_DATA_CAPABILITY.getDefaultInstance();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability==PLAYER_DATA_CAPABILITY;
    }

    @Override
    public <T> @Nullable T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability==PLAYER_DATA_CAPABILITY ? PLAYER_DATA_CAPABILITY.cast(this.impl) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return (NBTTagCompound)PLAYER_DATA_CAPABILITY.getStorage().writeNBT(PLAYER_DATA_CAPABILITY,this.impl,null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        PLAYER_DATA_CAPABILITY.getStorage().readNBT(PLAYER_DATA_CAPABILITY,this.impl,null, nbt);
    }
}