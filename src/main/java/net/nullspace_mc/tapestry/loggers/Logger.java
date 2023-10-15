package net.nullspace_mc.tapestry.loggers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.text.Text;

public abstract class Logger {

    private final Set<String /* Name of the player */> playersSubscribed;
    private final Map<String /* Name of the player */, String /* Name of the channel */> channelSubscriptions;
    private final Set<String /* Name of the channel */> availableChannels;
    private final boolean isAChannelRequired;

    public Logger(Set<String> channels, boolean isAChannelRequired) {
        this.playersSubscribed = new HashSet<>();
        this.channelSubscriptions = new HashMap<>();
        this.availableChannels = channels;
        this.isAChannelRequired = isAChannelRequired;
    }

    public Logger() {
        this(new HashSet<>(), false);
    }

    public abstract String getName();

    public abstract Text tickLogger(MinecraftServer server, ServerPlayerEntity player);

    public boolean isPlayerSubscribed(String playerName) {
        return playersSubscribed.contains(playerName);
    }

    public void subscribePlayer(String playerName) {
        playersSubscribed.add(playerName);
    }

    public void unsubscribePlayer(String playerName) {
        playersSubscribed.remove(playerName);
        channelSubscriptions.remove(playerName);
    }

    public Set<String> getPlayersSubscribed() {
        return playersSubscribed;
    }

    public Set<String> getAvailableChannels() {
        return availableChannels;
    }

    public Map<String, String> getChannelSubscriptions() {
        return channelSubscriptions;
    }

    public boolean getIsChannelRequired() {
        return isAChannelRequired;
    }

    public void onLogCommand(String playerName) {
        if (isPlayerSubscribed(playerName)) unsubscribePlayer(playerName);
        else subscribePlayer(playerName);
    }

    public void onLogCommand(String playerName, String channel) {
        if (isPlayerSubscribed(playerName) && channelSubscriptions.containsKey(playerName) && !channelSubscriptions.get(playerName).equals(channel)) // Player is logged, but changes channel
            channelSubscriptions.put(playerName, channel);

        else if (!isPlayerSubscribed(playerName)) { // Player isn't logged
            subscribePlayer(playerName);
            channelSubscriptions.put(playerName, channel);
        }

        else { // Player is logged, gets unsubscribed
            unsubscribePlayer(playerName);
            channelSubscriptions.remove(playerName);
        }
    }
}
