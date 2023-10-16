package net.nullspace_mc.tapestry.loggers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.text.Formatting;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class TNTLogger extends Logger {

    @Override
    public String getName() {
        return "tnt";
    }

    @Override
    public Text tickLogger(MinecraftServer server, ServerPlayerEntity player) {
        return null;
    }

    @Override
    public void subscribePlayer(String playerName) {
        super.subscribePlayer(playerName);
        ServerPlayerEntity player = MinecraftServer.getInstance().getPlayerManager().get(playerName);
        if (player != null) {
            Text t = new LiteralText("Subscribed to logger: tnt");
            t.setStyle(t.getStyle().setColor(Formatting.GRAY));
            player.sendMessage(t);
        }
    }

    @Override
    public void unsubscribePlayer(String playerName) {
        super.unsubscribePlayer(playerName);
        ServerPlayerEntity player = MinecraftServer.getInstance().getPlayerManager().get(playerName);
        if (player != null) {
            Text t = new LiteralText("Unsubscribed from logger: tnt");
            t.setStyle(t.getStyle().setColor(Formatting.GRAY));
            player.sendMessage(t);
        }
    }

    public void log(Text text) {
        for (String playername : this.getPlayersSubscribed()) {
            ServerPlayerEntity player = MinecraftServer.getInstance().getPlayerManager().get(playername);
            if (player != null && text != null) {
                player.sendMessage(text);
            }
        }
    }
}
