package me.day.cannoningutilities.features;

import me.day.cannoningutilities.config.Settings;
import me.day.cannoningutilities.utils.BoxOutlineGizmo;
import net.minecraft.client.Minecraft;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.phys.AABB;

public class BoxOutlinesTNT {
    public static void render(float delta) {
        if (!Settings.ENABLED) return;

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return;

        for (Entity entity : minecraft.level.entitiesForRendering()) {
            if (entity instanceof PrimedTnt) {
                double x = Mth.lerp(delta, entity.xo, entity.getX()) - entity.getX();
                double y = Mth.lerp(delta, entity.yo, entity.getY()) - entity.getY();
                double z = Mth.lerp(delta, entity.zo, entity.getZ()) - entity.getZ();

                AABB box = entity.getBoundingBox().move(x, y, z);
                Gizmos.addGizmo(new BoxOutlineGizmo(box, 0xFF00FFCC)).setAlwaysOnTop();
            }
        }
    }
}
