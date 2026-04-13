package me.day.cannoningutilities;

import me.day.cannoningutilities.config.Settings;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CannoningUtilities implements ClientModInitializer {
    public static String MOD_ID = "cannoningutilities";
    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Cannoning Utilities");
        Settings.INSTANCE.preload();
    }
}
