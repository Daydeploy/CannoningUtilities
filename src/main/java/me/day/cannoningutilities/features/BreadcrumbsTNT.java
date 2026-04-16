package me.day.cannoningutilities.features;

import com.mojang.blaze3d.vertex.*;
import me.day.cannoningutilities.config.Settings;
import me.day.cannoningutilities.utils.Crumb;
import me.day.cannoningutilities.utils.TrackedEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class BreadcrumbsTNT {
    private static final Map<UUID, TrackedEntity> trackedEntities = new ConcurrentHashMap<>();
    private static final Minecraft minecraft = Minecraft.getInstance();

    private static @Nullable BufferBuilder breadcrumbBuffer;

    private static void drawMarker(BufferBuilder builder, Vec3 position, float r, float g, float b, double size) {
        builder.addVertex((float) (position.x - size), (float) position.y, (float) position.z).setColor(r, g, b, 1.0f);
        builder.addVertex((float) (position.x + size), (float) position.y, (float) position.z).setColor(r, g, b, 1.0f);

        builder.addVertex((float) position.x, (float) (position.y - size), (float) position.z).setColor(r, g, b, 1.0f);
        builder.addVertex((float) position.x, (float) (position.y + size), (float) position.z).setColor(r, g, b, 1.0f);

        builder.addVertex((float) position.x, (float) position.y, (float) (position.z - size)).setColor(r, g, b, 1.0f);
        builder.addVertex((float) position.x, (float) position.y, (float) (position.z + size)).setColor(r, g, b, 1.0f);
    }

    public static @Nullable MeshData buildBreadcrumbs() {
        if (breadcrumbBuffer == null) return null;

        MeshData mesh = breadcrumbBuffer.build();
        breadcrumbBuffer = null;
        return mesh;
    }

    public static void clearBreadcrumbs() {
        trackedEntities.clear();
    }

    public static int getTrackedEntityCount() {
        return trackedEntities.size();
    }

    public static int getTotalTrackedEntityCount() {
        return trackedEntities.values().stream().mapToInt(t -> t.crumbs.size()).sum();
    }

    public static void tick() {
        if (!Settings.ENABLED) return;

        if (minecraft.level == null) {
            trackedEntities.clear();
            return;
        }

        if (Settings.REMOVAL_TIME_SECONDS > 0) {
            trackedEntities.entrySet().removeIf(entry -> entry.getValue().isExpired());
        }

        for (Entity entity : minecraft.level.entitiesForRendering()) {
            if (entity instanceof PrimedTnt || entity instanceof FallingBlockEntity) {
                UUID id = entity.getUUID();

                TrackedEntity trackedEntity = trackedEntities.computeIfAbsent(id, x -> new TrackedEntity(entity));

                Vec3 position = entity.position();
                if (trackedEntity.crumbs.getLast().position().equals(position)) {
                    trackedEntity.addCrumb(position);
                }
            }
        }
    }

    public static void extractBreadcrumbs() {
        if (!Settings.ENABLED || !Settings.RENDER_CRUMBS || trackedEntities.isEmpty()) return;

        Tesselator tessellator = Tesselator.getInstance();
        breadcrumbBuffer = tessellator.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

        Vec3 camera = minecraft.gameRenderer.getMainCamera().position();

        for (TrackedEntity trackedEntity : trackedEntities.values()) {
            if (trackedEntity.crumbs.size() < 2) continue;
            int color = trackedEntity.getColor();
            float r = ((color >> 16) & 0xFF) / 255f;
            float g = ((color >> 8) & 0xFF) / 255f;
            float b = (color & 0xFF) / 255f;

            for (int i = 0; i < trackedEntity.crumbs.size() - 1; i++) {
                Crumb current = trackedEntity.crumbs.get(i);
                Crumb next = trackedEntity.crumbs.get(i + 1);

                Vec3 position1 = current.position().subtract(camera);
                Vec3 position2 = next.position().subtract(camera);

                breadcrumbBuffer.addVertex((float) position1.x, (float) position1.y, (float) position1.z).setColor(r, g, b, 1.0F);
                breadcrumbBuffer.addVertex((float) position2.x, (float) position2.y, (float) position2.z).setColor(r, g, b, 1.0F);
            }
            drawMarker(breadcrumbBuffer, trackedEntity.startPosition.subtract(camera), 0.0F, 1.0F, 0.0F, 0.05);
            if (!trackedEntity.crumbs.isEmpty()) {
                Vec3 end = trackedEntity.crumbs.getLast().position().subtract(camera);
                drawMarker(breadcrumbBuffer, end, 0.0F, 1.0F, 1.0F, 0.05);
            }

        }
    }
}
