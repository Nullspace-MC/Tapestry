package net.nullspace_mc.tapestry.settings;

import net.fabricmc.loader.api.FabricLoader;
import net.nullspace_mc.tapestry.Tapestry;
import net.nullspace_mc.tapestry.helpers.TickSpeedHelper;

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
            category = {RuleCategory.CREATIVE, RuleCategory.FEATURE}
    )
    @RuleDefaults.Creative
    public static boolean alwaysEatCake = false;

    @Rule(
            desc = "Disables arrow despawning",
            category = RuleCategory.CREATIVE
    )
    public static boolean arrowsPersist = false;

    @Rule(
            desc = "Adds improved tab completions to vanilla commands",
            category = {RuleCategory.CREATIVE, RuleCategory.FEATURE}
    )
    @RuleDefaults.Creative
    public static boolean betterCompletions = false;

    @Rule(
            desc = "Enables filling cauldrons with water bottles",
            category = {RuleCategory.SURVIVAL, RuleCategory.CREATIVE, RuleCategory.FEATURE}
    )
    @RuleDefaults.Survival
    @RuleDefaults.Creative
    public static boolean bottleFillCauldron = false;

    @Rule(
            desc = "Generates superflat worlds with a chunk-aligned checkerboard pattern",
            extra = "For superflat presets with 2 layers, the layers will be exchanged for the alternate-colored chunks",
            category = {RuleCategory.CREATIVE, RuleCategory.FEATURE}
    )
    public static boolean chunkPattern = false;

    @Rule(
            desc = "Enables the clone command",
            extra = "Recommended to also enable fillOrientationFix",
            category = RuleCategory.COMMAND
    )
    public static boolean commandClone = true;

    @Rule(
            desc = "Enables the counter command",
            category = RuleCategory.COMMAND
    )
    public static boolean commandCounter = true;

    @Rule(
            desc = "Enables the fill command",
            extra = "Recommended to also enable fillOrientationFix",
            category = RuleCategory.COMMAND
    )
    public static boolean commandFill = true;

    @Rule(
            desc = "Enables the info command",
            category = RuleCategory.COMMAND
    )
    public static boolean commandInfo = true;

    @Rule(
            desc = "Enables the log command",
            category = RuleCategory.COMMAND
    )
    public static boolean commandLog = true;

    @Rule(
            desc = "Enables the tick command",
            category = RuleCategory.COMMAND,
            validator = CommandTickValidator.class
    )
    public static boolean commandTick = true;

    static class CommandTickValidator extends Validator<Boolean> {
        @Override
        boolean validate(Boolean value) {
            if(!value) {
                if(TickSpeedHelper.warping) {
                    TickSpeedHelper.stopTickWarp();
                }
                TickSpeedHelper.setTickRate(20);
            }
            return true;
        }
    }

    @Rule(
            desc = "Enables creative player noclip with a compatible client-side mod",
            category = {RuleCategory.CREATIVE, RuleCategory.FEATURE}
    )
    @RuleDefaults.Creative
    public static boolean creativeNoClip = false;

    @Rule(
            desc = "Enables/Disables explosion block breaking",
            category = RuleCategory.CREATIVE
    )
    public static boolean explosionBlockBreaking = true;

    @Rule(
            desc = "Volume limit of the fill/clone commands",
            category = RuleCategory.CREATIVE,
            options = {"32768", "250000", "1000000"},
            validator = NonNegativeValidator.class
    )
    @RuleDefaults.Creative("1000000")
    public static int fillLimit = 32768;

    @Rule(
            desc = "Fixes the orientation of chests, furnaces, etc. placed via command",
            extra = "Specifically, this fixes MC-31365",
            category = {RuleCategory.CREATIVE, RuleCategory.FIX}
    )
    @RuleDefaults.Creative
    @RuleDefaults.BugFix
    public static boolean fillOrientationFix = false;

    @Rule(
            desc = "Determines whether fill/clone/setblock send block updates",
            category = RuleCategory.CREATIVE
    )
    public static boolean fillUpdates = true;

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
            category = {RuleCategory.CREATIVE, RuleCategory.FEATURE}
    )
    @RuleDefaults.Creative
    public static boolean hopperCounter = false;

    @Rule(
            desc = "Enables instant command blocks",
            extra = "A command block will run instantly if it is on top of a redstone ore block",
            category = {RuleCategory.CREATIVE, RuleCategory.FEATURE}
    )
    public static boolean instantCommandBlock = false;

    @Rule(
            desc = "Causes gravity-affected blocks to fall instantly",
            category = RuleCategory.CREATIVE
    )
    public static boolean instantFall = false;

    @Rule(
            desc = "Instantly executes scheduled ticks",
            category = RuleCategory.CREATIVE
    )
    public static boolean instantScheduling = false;

    @Rule(
            desc = "Enables interoperability with KaboPC's Village Marker Mod",
            extra = "Players must relog for changes to take effect",
            category = {RuleCategory.CREATIVE, RuleCategory.FEATURE},
            validator = KVMValidator.class
    )
    @RuleDefaults.Creative
    public static boolean kaboVillageMarker = false;

    static class KVMValidator extends Validator<Boolean> {
		@Override
		boolean validate(Boolean value) {
			if (value == Boolean.TRUE) {
				if (FabricLoader.getInstance().isModLoaded("kabo-village-marker")) {
					Tapestry.LOGGER.info("The \'kaboVillageMarker\' setting is not compatible with the Kabo Village Marker Ornithe mod, and will be disabled.");
					Tapestry.LOGGER.info("If you wish to use the original Village Marker mod, remove the Kabo Village Marker Ornithe mod.");
					return false;
				}
			}
			return value;
		}
    }

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
            desc = "Enables placing already filled cauldrons",
            extra = "Placing a cauldron named with a number will cause it to get that number as fill level",
            category = {RuleCategory.CREATIVE, RuleCategory.FEATURE}
    )
    @RuleDefaults.Creative
    public static boolean placeFilledCauldron = false;

    @Rule(
            desc = "Makes redstone dust update order random",
            category = RuleCategory.CREATIVE
    )
    public static boolean randomRedstoneDust = false;

    @Rule(
            desc = "Sets the random tick speed",
            category = RuleCategory.CREATIVE,
            options = {"0", "3", "30000"},
            validator = NonNegativeValidator.class
    )
    public static int randomTickSpeed = 3;

    @Rule(
            desc = "Allows repeaters to have half of their usual delay",
            extra = "A repeater's delay is halved if it is on top of a redstone ore block",
            category = {RuleCategory.CREATIVE, RuleCategory.FEATURE}
    )
    public static boolean repeaterHalfDelay = false;

    @Rule(
            desc = "Enables repeating command blocks",
            extra = "A command block will run every tick if it is on top of a diamond ore block",
            category = {RuleCategory.CREATIVE, RuleCategory.FEATURE}
    )
    public static boolean repeatingCommandBlock = false;

    @Rule(
            desc = "Enables chunk unloading when the world is saved",
            extra = "Behaves like 1.7.5+",
            category = {RuleCategory.FEATURE, RuleCategory.SURVIVAL}
    )
    public static boolean saveUnloadChunks = false;

    @Rule(
            desc = "Forces connections to send small packets immediately instead of buffering them",
            extra = "Reduces latency in some cases",
            category = {RuleCategory.FIX}
    )
    public static boolean tcpNoDelay = false;

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
