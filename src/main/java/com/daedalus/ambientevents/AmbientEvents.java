package com.daedalus.ambientevents;

import com.daedalus.ambientevents.capability.player.AmbientEventsPlayerData;
import com.daedalus.ambientevents.capability.player.AmbientEventsPlayerDataStorage;
import com.daedalus.ambientevents.capability.player.IAmbientEventsPlayerData;
import com.daedalus.ambientevents.common.AmbientEventsCommands;
import com.daedalus.ambientevents.common.EventManager;
import com.daedalus.ambientevents.network.PacketSyncConfigData;
import mods.thecomputerizer.theimpossiblelibrary.network.NetworkHandler;
import mods.thecomputerizer.theimpossiblelibrary.util.file.FileUtil;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Objects;
import java.util.Random;

/**
 * I didn't feel like making more util classes beyond ParsingUtils so some misc util methods are here.
 */
@SuppressWarnings("unused")
@Mod(modid = AmbientEventsRef.MODID, name = AmbientEventsRef.NAME, version = AmbientEventsRef.VERSION,
		dependencies = AmbientEventsRef.DEPENDENCIES)
public class AmbientEvents {

	private static String configPath;

	public AmbientEvents() {
		NetworkHandler.queueClientPacketRegister(PacketSyncConfigData.class);
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		CapabilityManager.INSTANCE.register(IAmbientEventsPlayerData.class,new AmbientEventsPlayerDataStorage(),
				AmbientEventsPlayerData::new);
		configPath = "config/"+AmbientEventsRef.NAME;
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		if(Objects.isNull(configPath)) logFatal("Could not locate config directory?");
	}

	@Mod.EventHandler
	public static void onServerStarting(FMLServerStartingEvent e) {
		e.registerServerCommand(new AmbientEventsCommands());
		EventManager.loadConfig();
	}

	public static void chatMsg(@Nullable EntityPlayer player, String msg, boolean isLangKey, Object ... parameters) {
		if(Objects.isNull(player)) logError("Cannot send message {} to null player!",msg);
		else player.sendMessage(getText(msg,isLangKey,parameters));
	}

	public static FMLCommonHandler commonHandler() {
		return FMLCommonHandler.instance();
	}

	/**
	 * Maybe it would be better to use an access transformer and make Entity#rand public but this works just as well.
	 */
	public static Random entityRand(@Nullable Entity entity) {
		return Objects.nonNull(entity) ? (Objects.nonNull(entity.world) ? entity.world.rand : new Random()) : new Random();
	}

	/**
	 * Executes a server command
	 */
	public static void executeCommand(String rawCommand) {
		MinecraftServer server = getServer();
		executeCommand(server,server,rawCommand);
	}

	/**
	 * Executes a command as a specific sender.
	 */
	public static void executeCommand(ICommandSender sender, String rawCommand) {
		executeCommand(getServer(),sender,rawCommand);
	}

	/**
	 * Executes a command as a specific sender. The server is nullable since getServer() returns null on the client.
	 */
	public static void executeCommand(@Nullable MinecraftServer server, ICommandSender sender, String rawCommand) {
		if(Objects.isNull(server)) logError("Cannot execute command on null server");
		else server.commandManager.executeCommand(sender,rawCommand);
	}

	public static File getConfigFile(String path, boolean shouldOverride) {
		return FileUtil.generateNestedFile(configPath+"/"+path,shouldOverride);
	}

	public static ResourceLocation getResource(String path) {
		return new ResourceLocation(AmbientEventsRef.MODID,path);
	}

	/**
	 * MinecraftServer instances only exist on the server side unless it's a singleplayer world,
	 * but that can't be assumed.
	 */
	public static @Nullable MinecraftServer getServer() {
		if(getSide(true).isClient()) {
			logFatal( "Cannot get server instance on the client side! Things might be broken!");
			return null;
		}
		return commonHandler().getMinecraftServerInstance();
	}

	public static Side getSide(boolean effective) {
		return effective ? commonHandler().getEffectiveSide() : commonHandler().getSide();
	}

	public static ITextComponent getText(String msg, boolean isLangKey, Object ... parameters) {
		return isLangKey ? new TextComponentTranslation(msg,parameters) : new TextComponentString(msg);
	}

	/**
	 * Constructs and returns a lang key
	 */
	public static String lang(String type, String ... extras) {
		StringBuilder builder = new StringBuilder(type+"."+AmbientEventsRef.MODID+".");
		for(int i=0; i<extras.length; i++) {
			builder.append(extras[i]);
			if(i+1<extras.length) builder.append(".");
		}
		return builder.toString();
	}

	/**
	 * I didn't really need log wrappers, but it's fewer characters I guess.
	 */
	public static void logDebug(String msg, Object ... parameters) {
		AmbientEventsRef.LOGGER.debug(msg,parameters);
	}

	public static void logInfo(String msg, Object ... parameters) {
		AmbientEventsRef.LOGGER.info(msg,parameters);
	}

	public static void logWarn(String msg, Object ... parameters) {
		AmbientEventsRef.LOGGER.warn(msg,parameters);
	}

	public static void logError(String msg, Object ... parameters) {
		AmbientEventsRef.LOGGER.error(msg,parameters);
	}

	public static void logFatal(String msg, Object ... parameters) {
		AmbientEventsRef.LOGGER.fatal(msg,parameters);
	}

	public static void statusMsg(@Nullable EntityPlayer player, String msg, boolean isLangKey, boolean actionBar,
								 Object ... parameters) {
		if(Objects.isNull(player)) logError("Cannot send message {} to null player!",msg);
		else player.sendStatusMessage(getText(msg,isLangKey,parameters),actionBar);
	}
}
