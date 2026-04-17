package me.day.cannoningutilities.features;

import me.day.cannoningutilities.config.Settings;
import me.day.cannoningutilities.utils.TrackedEntity;
import me.day.cannoningutilities.utils.TrackedEntityGizmo;
import net.minecraft.client.Minecraft;
import net.minecraft.gizmos.GizmoProperties;
import net.minecraft.gizmos.Gizmos;
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

    public static void render() {
        if (!Settings.ENABLED || !Settings.RENDER_CRUMBS || entities.isEmpty()) return;

        for (TrackedEntity entity : entities.values()) {
            if (entity.crumbs.size() < 2) continue;

            GizmoProperties gizmo = Gizmos.addGizmo(new TrackedEntityGizmo(entity));
            if (Settings.DEPTH) gizmo.setAlwaysOnTop();
        }
    }
}
