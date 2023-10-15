package net.nullspace_mc.tapestry.loggers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.text.Formatting;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.*;

public class ProjectileLogger extends Logger {

    @Override
    public String getName() {
        return "projectiles";
    }

    @Override
    public Text tickLogger(MinecraftServer server, ServerPlayerEntity player) {
        return null;
    }

    @Override
    public boolean getIsChannelRequired() {
        return true;
    }

    @Override
    public Set<String> getAvailableChannels() {
        return new HashSet<>(Arrays.asList("brief", "full"));
    }

    @Override
    public void onLogCommand(String playerName, String channel) {
        if ((isPlayerSubscribed(playerName) && this.getChannelSubscriptions().containsKey(playerName) && !this.getChannelSubscriptions().get(playerName).equals(channel))
                || !isPlayerSubscribed(playerName)) {
            ServerPlayerEntity player = MinecraftServer.getInstance().getPlayerManager().get(playerName);
            if (player != null) {
                Text t = new LiteralText("Subscribed to logger: projectiles (" + channel + ")");
                t.setStyle(t.getStyle().setColor(Formatting.GRAY));
                player.sendMessage(t);
            }
        } else {
            ServerPlayerEntity player = MinecraftServer.getInstance().getPlayerManager().get(playerName);
            if (player != null) {
                Text t = new LiteralText("Unsubscribed from logger: projectiles");
                t.setStyle(t.getStyle().setColor(Formatting.GRAY));
                player.sendMessage(t);
            }
        }
        super.onLogCommand(playerName, channel);
    }

    public void log(Text text, String channel) {
        for (String playername : this.getPlayersSubscribed()) {
            ServerPlayerEntity player = MinecraftServer.getInstance().getPlayerManager().get(playername);
            if (player != null && text != null) {
                if (this.getChannelSubscriptions().get(playername).equals(channel)) player.sendMessage(text);
            }
        }
    }
}