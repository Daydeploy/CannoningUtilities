package me.day.cannoningutilities;

import me.day.cannoningutilities.config.Settings;
import me.day.cannoningutilities.features.BreadcrumbsTNT;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CannoningUtilities implements ClientModInitializer {
    public static String MOD_ID = "cannoningutilities";
    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Cannoning Utilities");
        Settings.INSTANCE.preload();

        ClientTickEvents.END_CLIENT_TICK.register(client -> BreadcrumbsTNT.tick());

        WorldRenderEvents.LAST.register(this::renderBreadcrumbs);
    }

    private void renderBreadcrumbs(WorldRenderContext context) {
        BreadcrumbsTNT.renderBreadcrumbs(context.matrixStack(), context.camera().getPosition());
    }
}
