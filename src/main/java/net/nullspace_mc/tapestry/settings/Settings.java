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
        desc = "Enables/Disables explosion block breaking",
        category = RuleCategory.CREATIVE
    )
    public static boolean explosionBlockBreaking = true;

    @Rule(
        desc = "Fix that allows nether brick spawning in all fortresses",
        category = RuleCategory.FIX
    )
    @RuleDefaults.Survival
    @RuleDefaults.Creative
    @RuleDefaults.BugFix
    public static boolean fortressSpawningFix = false;

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
