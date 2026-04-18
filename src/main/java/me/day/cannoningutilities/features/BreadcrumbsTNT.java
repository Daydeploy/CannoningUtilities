package me.day.cannoningutilities.features;

import me.day.cannoningutilities.config.Settings;
import me.day.cannoningutilities.utils.BoxOutlineGizmo;
import me.day.cannoningutilities.utils.TrackedEntity;
import me.day.cannoningutilities.utils.TrackedEntityGizmo;
import net.minecraft.client.Minecraft;
import net.minecraft.gizmos.Gizmo;
import net.minecraft.gizmos.GizmoProperties;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class BreadcrumbsTNT {
    private static final ConcurrentHashMap<UUID, TrackedEntity> entities = new ConcurrentHashMap<>();
    private static final Minecraft minecraft = Minecraft.getInstance();

    public static void tick() {
        if (!Settings.ENABLED) return;

        if (minecraft.level == null) {
            entities.clear();
            return;
        }

        if (Settings.REMOVAL_TIME_SECONDS > 0) entities.entrySet().removeIf(entry -> entry.getValue().isExpired());

        for (Entity entity : minecraft.level.entitiesForRendering()) {
            if (entity instanceof PrimedTnt || entity instanceof FallingBlockEntity) {
                UUID id = entity.getUUID();
                TrackedEntity trackedEntity = entities.computeIfAbsent(id, x -> new TrackedEntity(entity));

                Vec3 position = entity.position();
                if (!trackedEntity.crumbs.getLast().position().equals(position)) trackedEntity.addCrumb(position);
            }
        }

    }

    public static void render(float delta) {
        if (!Settings.ENABLED || minecraft.level == null || entities.isEmpty()) return;

        if (Settings.RENDER_CRUMBS) {
            for (TrackedEntity entity : entities.values()) {
                if (entity.crumbs.size() < 2) continue;

                drawGizmo(new TrackedEntityGizmo(entity));
            }
        }

        if (Settings.SHOW_EXPLOSION_BLOCK) {
            for (Entity entity : minecraft.level.entitiesForRendering()) {
                if (entity instanceof PrimedTnt) {
                    double x = Mth.lerp(delta, entity.xo, entity.getX()) - entity.getX();
                    double y = Mth.lerp(delta, entity.yo, entity.getY()) - entity.getY();
                    double z = Mth.lerp(delta, entity.zo, entity.getZ()) - entity.getZ();
                    drawGizmo(new BoxOutlineGizmo(entity.getBoundingBox().move(x, y, z), 0xFF00FFCC));
                }
            }
        }
    }

    private static void drawGizmo(Gizmo gizmo) {
        GizmoProperties properties = Gizmos.addGizmo(gizmo);
        if (Settings.DEPTH) properties.setAlwaysOnTop();
    }
}
