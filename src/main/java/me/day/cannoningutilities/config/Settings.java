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
    @Switch(title = "Enabled", description = "", category = "", subcategory = "")
    public static boolean ENABLED = true;
    @Slider(title = "Removal Time", description = "", category = "Breadcrumbs", subcategory = "", min = 0F, max = 60F, step = 1F)
    public static int REMOVAL_TIME_SECONDS = 0;
    @Number(title = "Line Width", description = "", category = "Breadcrumbs", subcategory = "", min = 0F, max = 10F)
    public static float LINE_WIDTH = 1.0F;
    @Switch(title = "Depth", description = "", category = "Breadcrumbs", subcategory = "")
    public static boolean DEPTH = false;
    @Switch(title = "Display", description = "", category = "Breadcrumbs", subcategory = "")
    public static boolean RENDER_CRUMBS = true;
    @Switch(title = "Explosion Block", description = "", category = "Breadcrumbs", subcategory = "")
    public static boolean SHOW_EXPLOSION_BLOCK = false;
    @Dropdown(title = "TNT Rendering", description = "", category = "Minimal TNT", subcategory = "Rendering")
    public static Rendering TNT_RENDERING = Rendering.ENABLED;
    @Dropdown(title = "TNT Explosions", description = "", category = "Minimal TNT", subcategory = "Explosions")
    public static Explosions TNT_EXPLOSIONS = Explosions.ENABLED;

    public Settings() {
        super("/cannoningutilities/config.json", "/assets/cannoningutilities/icon.png", "Cannoning Utilities", Category.QOL);
    }
}
