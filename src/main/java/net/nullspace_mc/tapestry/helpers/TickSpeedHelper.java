package net.nullspace_mc.tapestry.helpers;

import net.minecraft.server.MinecraftServer;

public class TickSpeedHelper {
    private static long tickRate = 20;
    private static long tickWarpingEnd;

    public static long getTickLength() {
        return 1000 / tickRate;
    }

    public static void setTickRate(long value) {
        tickRate = value;
    }

    public static void startTickWarp(long duration) {
        if (duration == 0) {
            tickRate = 20;
            return;
        }
        tickRate = 500;
        tickWarpingEnd = MinecraftServer.getServer().getTicks() + Math.abs(duration);
    }

    public static void refresh() {
        if (MinecraftServer.getServer().getTicks() == tickWarpingEnd) tickRate = 20;
    }
}
