package org.example.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Entrypoint
{
    public static final Logger LOGGER = LogManager.getLogger();

    public static void initialize() {
        LOGGER.info("Initializing");
    }
}
