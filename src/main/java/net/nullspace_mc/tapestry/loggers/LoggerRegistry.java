package net.nullspace_mc.tapestry.loggers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.nullspace_mc.tapestry.settings.Settings;
import net.nullspace_mc.tapestry.util.MathUtil;

public class LoggerRegistry {

    private static final Map<String /* Logger name */, Logger /* Logger */> loggerRegistry = new HashMap<>();

    /**
     * Registers all loggers.
     */
    public static void registerAllLoggers() {
        registerLogger(new TPSLogger());
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
        MinecraftServer server = MinecraftServer.getServer();
        if (server.getTicks() % Settings.loggerRefreshRate == 0) updateHudLoggers(server);
    }

    private static void appendArrayListOfText(Text text, ArrayList<Text> array) {
        if (array.isEmpty()) return;
        Iterator<Text> ite = array.iterator();
        Text textToAppend = ite.next();
        while (true) {
            boolean isTextEmpty = textToAppend.getString().isEmpty();
            if (!isTextEmpty) text.append(textToAppend);
            if (ite.hasNext()) {
                textToAppend = ite.next();
                if (!isTextEmpty && !textToAppend.getString().isEmpty()) text.append("\n");
            } else break;
        }
    }

    @SuppressWarnings("unchecked")
    private static void updateHudLoggers(MinecraftServer server) {
        for (Object element : server.getPlayerManager().players) {
            if (!(element instanceof ServerPlayerEntity)) continue;
            ServerPlayerEntity player = (ServerPlayerEntity) element;
            Text info = new LiteralText("");
            ArrayList<Text> hudUpdate = new ArrayList<>();
            for(Logger logger : LoggerRegistry.loggerRegistry.values()) {
                if(logger.isPlayerSubscribed(player.getName().getString())) {
                    hudUpdate.add(logger.tickLogger(server, player));
                }
            }

            // Concatenate all information and send to the client
            appendArrayListOfText(info, hudUpdate);
            if (info.getString().isEmpty() && info.getSiblings().stream().allMatch(sibling -> ((Text)sibling).getString().isEmpty())) // If the content of info is empty, no need to send anything to the client
                return;
            player.networkHandler.sendPacket(new GameMessageS2CPacket(info));
        }
    }
}
