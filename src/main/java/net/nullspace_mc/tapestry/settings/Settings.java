package net.nullspace_mc.tapestry.settings;

/**
 * Place Tapestry rules in this class
 * Rules must be public static and have a type of one of:
 * - boolean
 * - int
 * - long
 * - float
 * - double
 * - String
 * The initial value of the field will be the default value of the rule
 */
public class Settings {
    @Rule(
            desc = "Allows player to always eat cake",
            category = RuleCategory.CREATIVE
    )
    public static boolean alwaysEatCake = false;

    @Rule(
        desc = "Disables arrow despawning",
        category = RuleCategory.CREATIVE
    )
    public static boolean arrowsPersist = false;

    @Rule(
        desc = "Generates superflat worlds with a chunk-aligned checkerboard pattern",
        extra = "For superflat presets with 2 layers, the layers will be exchanged for the alternate-colored chunks",
        category = RuleCategory.CREATIVE
    )
    public static boolean chunkPattern = false;

    @Rule(
        desc = "Enables creative player noclip with a compatible client-side mod",
        category = RuleCategory.CREATIVE
    )
    @RuleDefaults.Creative
    public static boolean creativeNoClip = false;

    @Rule(
        desc = "Enables/Disables explosion block breaking",
        category = RuleCategory.CREATIVE
    )
    public static boolean explosionBlockBreaking = true;

    @Rule(
        desc = "Fix that allows nether brick spawning in all fortresses",
        category = {RuleCategory.CREATIVE, RuleCategory.FIX}
    )
    @RuleDefaults.Survival
    @RuleDefaults.Creative
    @RuleDefaults.BugFix
    public static boolean fortressSpawningFix = false;

    @Rule(
            desc = "Enables hopper counters",
            category = RuleCategory.CREATIVE
    )
    @RuleDefaults.Creative
    public static boolean hopperCounter = false;

    @Rule(
            desc = "Enables instant command blocks",
            extra = "A command block will run instantly if it is on top of a redstone ore block",
            category = RuleCategory.CREATIVE
    )
    public static boolean instantCommandBlock = false;

    @Rule(
        desc = "Enables interoperability with KaboPC's Village Marker Mod",
        extra = "Players must relog for changes to take effect",
        category = RuleCategory.CREATIVE
    )
    @RuleDefaults.Creative
    public static boolean kaboVillageMarker = false;

    @Rule(
            desc = "Disables fluid flowing breaking blocks",
            category = RuleCategory.CREATIVE
    )
    public static boolean liquidDamageDisabled = false;

    @Rule(
            desc = "Sets the rate at which loggers refresh",
            category = RuleCategory.CREATIVE,
            validator = PositiveValidator.class
    )
    public static int loggerRefreshRate = 20;

    @Rule(
            desc = "Makes redstone dust update order random",
            category = RuleCategory.CREATIVE
    )
    public static boolean randomRedstoneDust = false;

    @Rule(
            desc = "Allows repeater to have half of their usual delay",
            extra = "A repeater's delay is halved if it is on top of a redstone ore block",
            category = RuleCategory.CREATIVE
    )
    public static boolean repeaterHalfDelay = false;

    @Rule(
            desc = "Enables repeating command blocks",
            extra = "A command block will run every tick if it is on top of a diamond ore block",
            category = RuleCategory.CREATIVE
    )
    public static boolean repeatingCommandBlock = false;

    static class PositiveValidator extends Validator<Integer> {
        @Override
        boolean validate(Integer value) {
            return value > 0;
        }
    }

    static class NonNegativeValidator extends Validator<Integer> {
        @Override
        boolean validate(Integer value) {
            return value >= 0;
        }
    }
}
