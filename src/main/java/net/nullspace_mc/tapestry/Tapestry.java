package net.nullspace_mc.tapestry;

import net.nullspace_mc.tapestry.counter.CounterRegistry;
import net.nullspace_mc.tapestry.helpers.TickSpeedHelper;
import net.nullspace_mc.tapestry.loggers.LoggerRegistry;
import net.nullspace_mc.tapestry.settings.SettingsManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Tapestry {

    public static final Logger LOGGER = LogManager.getLogger();

    public static void initialize() {
        LOGGER.info("Initializing");
        SettingsManager.parseRules();
        LoggerRegistry.registerAllLoggers();
        CounterRegistry.setupCounters();
    }

    public static void onStart() {
        LOGGER.info("Applying rules from tapestry.conf");
        SettingsManager.applyConf();
    }

    public static void onTick() {
        LoggerRegistry.tickLoggers();
    }
}
