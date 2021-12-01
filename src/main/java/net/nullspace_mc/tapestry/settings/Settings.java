package net.nullspace_mc.tapestry.settings;

public class Settings {
    @Rule(
        desc = "Fix that allows nether brick spawning in all fortresses",
        category = RuleCategory.FIX
    )
    @RuleDefaults.Survival
    @RuleDefaults.Creative
    @RuleDefaults.BugFix
    public static boolean fortressSpawningFix = false;

    private static boolean validatePositive(int value) {
        return value > 0;
    }

    private static boolean validateNonNegative(int value) {
        return value >= 0;
    }
}
