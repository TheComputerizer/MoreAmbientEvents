package com.daedalus.ambientevents.parsing.conditions;

import com.daedalus.ambientevents.AmbientEvents;
import com.daedalus.ambientevents.parsing.numbers.RawNumber;
import com.daedalus.ambientevents.parsing.strings.RawString;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import de.ellpeck.nyx.Nyx;
import de.ellpeck.nyx.capabilities.NyxWorld;
import de.ellpeck.nyx.lunarevents.LunarEvent;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import java.util.Objects;
import java.util.Random;

public class MoonCondition extends Condition {

    public MoonCondition(JsonObject json) throws JsonIOException {
        super();
        addNumber(json,"phase",new RawNumber(-1));
        addString(json,"nyxmoon",new RawString("any"));
    }

    public MoonCondition(ByteBuf buf) {
        super(buf);
    }

    @Override
    public boolean isMet(EntityPlayer player) {
        Random rand = AmbientEvents.entityRand(player);
        World world = player.getEntityWorld();
        return checkMoonPhase(rand,world) && checkNyxEvent(rand,world);
    }

    private boolean checkMoonPhase(Random rand, World world) {
        int phase = getNum(rand,"phase").intValue();
        return phase<0 || phase==world.getMoonPhase();
    }

    private boolean checkNyxEvent(Random rand, World world) {
        String eventName = getStr(rand,"nyxmoon").trim().toLowerCase();
        if(eventName.matches("any")) return true;
        if(!Loader.isModLoaded(Nyx.ID)) return false;
        LunarEvent event = NyxWorld.get(world).currentEvent;
        return Objects.nonNull(event) && event.name.matches(eventName);
    }
}
