package com.daedalus.ambientevents.parsing.conditions;

import com.daedalus.ambientevents.parsing.strings.RawString;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;

public class SubmergedCondition extends Condition {

    public SubmergedCondition(JsonObject json) throws JsonIOException {
        super();
        addString(json,"fluid",new RawString("minecraft:water"));
    }

    public SubmergedCondition(ByteBuf buf) {
        super(buf);
    }

    @Override
    public boolean isMet(EntityPlayer player) {
        AxisAlignedBB aabb = player.getEntityBoundingBox();
        Vec3d topCenter = new Vec3d(aabb.minX+((aabb.maxX-aabb.minX)/2d),aabb.maxY,aabb.minZ+((aabb.maxZ-aabb.minZ)/2d));
        IBlockState state = player.world.getBlockState(new BlockPos(topCenter));
        if(state.getBlock() instanceof IFluidBlock)
            return FluidRegistry.getDefaultFluidName(((IFluidBlock)state.getBlock()).getFluid()).matches(getStr(player,"fluid"));
        return false;
    }
}
