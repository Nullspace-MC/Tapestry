package net.nullspace_mc.tapestry.util;

import net.minecraft.util.math.Direction;

public class PositionUtil {


    /**
     * Offset the position given through x, y and z parameters, by 1 blocks in the desired direction
     *
     * @param x         X source position
     * @param y         Y source position
     * @param z         Z source position
     * @param direction Direction towards which offsetting
     * @return Returns an array of 3 ints, respectively the x, y and z position of the offset.
     */
    public static int[] offset(int x, int y, int z, Direction direction) {
        return offset(x, y, z, direction, 1);
    }

    /**
     * Offset the position given through x, y and z parameters, by amount blocks in the desired direction
     *
     * @param x         X source position
     * @param y         Y source position
     * @param z         Z source position
     * @param direction Direction towards which offsetting
     * @param amount    Amount of blocks to offset from source
     * @return Returns an array of 3 ints, respectively the x, y and z position of the offset.
     */
    public static int[] offset(int x, int y, int z, Direction direction, int amount) {
        x += direction.getXOffset() * amount;
        y += direction.getYOffset() * amount;
        z += direction.getZOffset() * amount;
        return new int[]{x, y, z};
    }
}
