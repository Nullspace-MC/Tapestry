package net.nullspace_mc.tapestry.counter;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.server.MinecraftServer;

public class Counter {

    private final Map<String /* Item name */, Integer /* Item count */> itemCounters = new HashMap<>();
    private int tickStart;
    private int totalCount;

    public Map<String, Integer> getCounterMap() {
        return itemCounters;
    }

    public void addToCounter(String itemName, int itemCount) {
        if (itemCounters.isEmpty()) tickStart = MinecraftServer.getServer().getTicks();
        if (!itemCounters.containsKey(itemName)) itemCounters.put(itemName, 0);
        itemCounters.put(itemName, itemCounters.get(itemName) + itemCount);
        totalCount += itemCount;
    }

    public void resetCounter() {
        tickStart = MinecraftServer.getServer().getTicks();
        itemCounters.clear();
        totalCount = 0;
    }

    public int getRunningTime() {
        return MinecraftServer.getServer().getTicks() - this.tickStart;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
