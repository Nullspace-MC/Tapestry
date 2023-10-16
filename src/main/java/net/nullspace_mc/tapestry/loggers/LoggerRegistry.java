package net.nullspace_mc.tapestry.loggers;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.text.Text;

import net.nullspace_mc.tapestry.settings.Settings;

public class LoggerRegistry {

    private static final Map<String /* Logger name */, Logger /* Logger */> loggerRegistry = new TreeMap<>();

    /**
     * Registers all loggers.
     */
    public static void registerAllLoggers() {
        registerLogger(new MobCapLogger());
        registerLogger(new TPSLogger());
        registerLogger(new ProjectileLogger());
        registerLogger(new TNTLogger());
    }

    private static void registerLogger(Logger logger) {
        loggerRegistry.put(logger.getName(), logger);
    }

    public static Logger getLoggerFromName(String name) {
        return loggerRegistry.get(name);
    }

    public static Set<String> getAllLoggersName() {
        return loggerRegistry.keySet();
    }

    public static void tickLoggers() {
        if (Settings.loggerRefreshRate == 0) return;
        MinecraftServer server = MinecraftServer.getInstance();
        if (server.getTicks() % Settings.loggerRefreshRate == 0) updateHudLoggers(server);
    }

    private static void updateHudLoggers(MinecraftServer server) {
        for (Object element : server.getPlayerManager().players) {
            if (!(element instanceof ServerPlayerEntity)) continue;
            ServerPlayerEntity player = (ServerPlayerEntity) element;
            for(Logger logger : LoggerRegistry.loggerRegistry.values()) {
                if(logger.isPlayerSubscribed(player.getName())) {
                    Text logMessage = logger.tickLogger(server, player);
                    if (logMessage == null) continue;
                    player.networkHandler.sendPacket(new ChatMessageS2CPacket(logMessage));
                }
            }
        }
    }
}
