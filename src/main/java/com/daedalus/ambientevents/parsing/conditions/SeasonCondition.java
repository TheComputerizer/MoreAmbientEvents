package com.daedalus.ambientevents.parsing.conditions;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

public class SeasonCondition extends Condition {

    public SeasonCondition(JsonObject json) throws JsonIOException {
        super();
        addString(json,"season");
    }

    public SeasonCondition(ByteBuf buf) {
        super(buf);
    }

    @Override
    public boolean isMet(EntityPlayer player) {
        Season season = SeasonHelper.getSeasonState(player.getEntityWorld()).getSeason();
        return normalizedSeasonName(player).matches(season.name().toLowerCase());
    }

    private String normalizedSeasonName(EntityPlayer player) {
        String id = getStr(player,"season").trim().toLowerCase();
        if(id.matches("0")) return "spring";
        if(id.matches("1")) return "summer";
        if(id.matches("2") || id.matches("fall")) return "autumn";
        if(id.matches("3")) return "winter";
        return id;
    }
}
