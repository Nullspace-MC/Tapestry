package net.nullspace_mc.tapestry.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil {
    public static double round(int places, double value) {
        return new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }

    public static String getFancyTime(double hours) {
        if (hours > 1) return MathUtil.round(2, hours) + "h";
        hours *= 60D;
        if (hours > 1) return MathUtil.round(2, hours) + "m";
        hours *= 60D;
        return MathUtil.round(2, hours) + "s";
    }
}
