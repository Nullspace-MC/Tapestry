package net.nullspace_mc.tapestry.loggers.helpers;

import net.minecraft.text.Formatting;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.nullspace_mc.tapestry.loggers.LoggerRegistry;
import net.nullspace_mc.tapestry.loggers.TNTLogger;

public class TNTLoggerHelper {

    private final TNTLogger logger;
    private double primedX, primedY, primedZ;
    private double veloX, veloY, veloZ;

    public TNTLoggerHelper() {
        logger = (TNTLogger)LoggerRegistry.getLoggerFromName("tnt");
    }

    public void onPrimed(double x, double y, double z, double vx, double vy, double vz) {
        primedX = x;
        primedY = y;
        primedZ = z;
        veloX = vx;
        veloY = vy;
        veloZ = vz;
    }

    public void onExploded(double x, double y, double z) {
        logger.log(format(primedX, primedY, primedZ, veloX, veloY, veloZ, x, y, z));
    }

    public Text format(double px, double py, double pz, double vx, double vy, double vz, double ex, double ey, double ez) {
        Text t = new LiteralText(
                String.format("P [ %.1f, %.1f, %.1f ] [ %.1f, %.1f, %.1f ] ",
                        px, py, pz,
                        vx, vy, vz));
        t.setStyle(t.getStyle().setColor(Formatting.GREEN));
        Text t2 = new LiteralText(
                String.format("E [ %.1f, %.1f, %.1f ]",
                        ex, ey, ez));
        t2.setStyle(t2.getStyle().setColor(Formatting.RED));
        t.append(t2);
        return t;
    }
}
