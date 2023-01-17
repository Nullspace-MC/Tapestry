package net.nullspace_mc.tapestry.helpers;

public class SetBlockFlags {

    /**
     * update neighboring blocks
     */
    public static final int UPDATE_NEIGHBORS         = 0b001;
    /**
     * sync the block change with clients
     */
    public static final int UPDATE_CLIENTS           = 0b010;
    /**
     * hide the block change from the world renderer
     */
    public static final int UPDATE_INVISIBLE         = 0b100;

}
