package me.day.cannoningutilities.config;

import me.day.cannoningutilities.core.Explosions;
import me.day.cannoningutilities.core.Rendering;
import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.annotations.Dropdown;
import org.polyfrost.oneconfig.api.config.v1.annotations.Number;
import org.polyfrost.oneconfig.api.config.v1.annotations.Slider;
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch;

public class Settings extends Config {
    public static final Settings INSTANCE = new Settings();
    @Switch(title = "Enabled", description = "Toggle Cannoning Utilities on/off", category = "", subcategory = "Breadcrumbs Options")
    public static boolean ENABLED = true;
    @Slider(title = "Removal Time", description = "Time in seconds before breadcrumbs are removed", category = "", subcategory = "Breadcrumbs Options", min = 0F, max = 60F, step = 1F)
    public static int REMOVAL_TIME_SECONDS = 30;
    @Number(title = "Line Width", description = "Width of the breadcrumbs tracer", category = "", subcategory = "Breadcrumbs Options", min = 0F, max = 10F)
    public static float LINE_WIDTH = 5F;
    @Switch(title = "Depth", description = "", category = "", subcategory = "Breadcrumbs Options")
    public static boolean DEPTH = true;
    @Switch(title = "Display", description = "Toggle breadcrumbs on/off", category = "", subcategory = "Breadcrumbs Options")
    public static boolean RENDER_CRUMBS = true;
    @Switch(title = "Explosion Block", description = "Toggle explosion blocks on/off", category = "", subcategory = "Breadcrumbs Options")
    public static boolean SHOW_EXPLOSION_BLOCK = true;
    @Dropdown(title = "Rendering", description = "Render blocks", category = "", subcategory = "Minimal TNT")
    public static Rendering TNT_RENDERING = Rendering.ENABLED;
    @Dropdown(title = "Explosions", description = "Render explosions", category = "", subcategory = "Minimal TNT")
    public static Explosions TNT_EXPLOSIONS = Explosions.ENABLED;

    public Settings() {
        super("/cannoningutilities/config.json", "/assets/cannoningutilities/icon.svg", "Cannoning Utilities", Category.QOL);
    }
}
