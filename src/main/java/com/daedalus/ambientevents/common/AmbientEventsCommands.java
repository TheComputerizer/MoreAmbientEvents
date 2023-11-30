package com.daedalus.ambientevents.common;

import com.daedalus.ambientevents.AmbientEvents;
import com.daedalus.ambientevents.AmbientEventsRef;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class AmbientEventsCommands extends CommandBase {

    public AmbientEventsCommands() {
        AmbientEvents.logInfo("Intializing commands");
    }

    @Override
    public String getName() {
        return AmbientEventsRef.MODID;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return AmbientEvents.lang("commands","usage");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String ... args) throws CommandException {
        if(args.length>=1 && args[0].matches("reload")) {
            EventManager.loadConfig();
            sendMessage(sender,AmbientEvents.lang("commands","reload","success"));
        }
        else sendMessage(sender,AmbientEvents.lang("commands","usage"));
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos target) {
        return args.length==1 && "reload".startsWith(args[0]) ? Collections.singletonList("reload") : Collections.emptyList();
    }

    private void sendMessage(ICommandSender sender, String lang, Object ... args) {
        sender.sendMessage(AmbientEvents.getText(lang,true,args));
    }
}
