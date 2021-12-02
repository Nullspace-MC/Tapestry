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
