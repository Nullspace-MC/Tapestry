package net.nullspace_mc.tapestry.loggers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Logger {

    private final String name;
    private final Set<String /* Name of the player */> playersSubscribed;
    private final Map<String /* Name of the player */, String /* Name of the channel */> channelSubscriptions;
    private final Set<String /* Name of the channel */> availableChannels;
    private final boolean isAChannelRequired;

    public Logger(String name, Set<String> channels, boolean isAChannelRequired) {
        this.name = name;
        this.playersSubscribed = new HashSet<>();
        this.channelSubscriptions = new HashMap<>();
        this.availableChannels = channels;
        this.isAChannelRequired = isAChannelRequired;
    }

    public Logger(String name) {
        this(name, new HashSet<>(), false);
    }

    public boolean isPlayerSubscribed(String playerName) {
        return playersSubscribed.contains(playerName);
    }

    public void subscibePlayer(String playerName) {
        playersSubscribed.add(playerName);
    }

    public void unsubscribePlayer(String playerName) {
        playersSubscribed.remove(playerName);
    }

    public String getName() {
        return name;
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
        else subscibePlayer(playerName);
    }

    public void onLogCommand(String playerName, String channel) {
        if (isPlayerSubscribed(playerName) && !channelSubscriptions.get(playerName).equals(channel)) // Player is logged, but changes channel
            channelSubscriptions.put(playerName, channel);

        else if (!isPlayerSubscribed(playerName)) { // Player isn't logged
            subscibePlayer(playerName);
            channelSubscriptions.put(playerName, channel);
        }

        else { // Player is logged, gets unsubscribed
            unsubscribePlayer(playerName);
            channelSubscriptions.remove(playerName);
        }
    }
}
