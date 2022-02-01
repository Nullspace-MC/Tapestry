package net.nullspace_mc.tapestry.helpers;

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;

public class TickSpeedHelper {

    public static double tickRate = 20D;
    public static long mspt = 50L;

    public static boolean warping = false;
    public static CommandSource warper = null;
    public static long ticksToWarp = 0L;
    public static long warpedTicks = 0L;
    public static long warpStartTimeMillis = 0L;

    public static void setTickRate(double tps) {
        tickRate = tps;
        mspt = (long)(1000L/tps);
    }

    public static void startTickWarp(CommandSource wpr, long ticks) {
        if (!warping) {
            warpStartTimeMillis = MinecraftServer.getTimeMillis();
        }
        warping = true;
        warper = wpr;
        ticksToWarp = ticks;
    }

    public static void stopTickWarp()
    {
        long elapsedTimeMillis = MinecraftServer.getTimeMillis() - warpStartTimeMillis;
        double warpMSPT = (double)elapsedTimeMillis / (double)warpedTicks;
        double warpTPS = 1000D / warpMSPT;

        AbstractCommand.sendFeedback(warper, String.format("Tick warp finished with %.1f tps (%.2f mspt)", warpTPS, warpMSPT), new Object[0]);

        warping = false;
        warper = null;
        ticksToWarp = 0L;
        warpedTicks = 0L;
        warpStartTimeMillis = 0L;
    }
}
