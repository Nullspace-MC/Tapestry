package net.nullspace_mc.tapestry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.nullspace_mc.tapestry.counter.CounterRegistry;
import net.nullspace_mc.tapestry.loggers.LoggerRegistry;
import net.nullspace_mc.tapestry.settings.SettingsManager;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;

public class Tapestry implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void init() {
        LOGGER.info("Initializing");
        SettingsManager.parseRules();
        LoggerRegistry.registerAllLoggers();
        CounterRegistry.setupCounters();

        MinecraftServerEvents.START.register(server -> {
            LOGGER.info("Applying rules from tapestry.conf");
            SettingsManager.applyConf();
        });
        MinecraftServerEvents.TICK_START.register(server -> {
            LoggerRegistry.tickLoggers();
        });
    }
}
