package net.nullspace_mc.tapestry;

import net.minecraft.server.MinecraftServer;
import net.nullspace_mc.tapestry.settings.SettingsManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Tapestry {
    public static final Logger LOGGER = LogManager.getLogger();

    public static void initialize() {
        LOGGER.info("Initializing");
        SettingsManager.parseRules();
    }

    public static void onSetLevelName() {
        LOGGER.info("Applying rules from tapestry.conf");
        SettingsManager.applyRulesFromConf();
    }
}
