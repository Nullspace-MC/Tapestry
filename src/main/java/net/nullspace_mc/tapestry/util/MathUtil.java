package net.nullspace_mc.tapestry.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil {
    public static double round(int places, double value) {
        return new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }
}
