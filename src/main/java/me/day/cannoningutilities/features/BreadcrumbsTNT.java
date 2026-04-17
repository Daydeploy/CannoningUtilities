package me.day.cannoningutilities.features;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import me.day.cannoningutilities.config.Settings;
import me.day.cannoningutilities.utils.Crumb;
import me.day.cannoningutilities.utils.TrackedEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class BreadcrumbsTNT {
    private static final Map<UUID, TrackedEntity> trackedEntities = new ConcurrentHashMap<>();
    private static final Minecraft minecraft = Minecraft.getInstance();

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
                if (trackedEntity.crumbs.isEmpty() || !trackedEntity.crumbs.getLast().position().equals(position)) {
                    trackedEntity.addCrumb(position);
                }
            }
        }
    }

    public static void renderBreadcrumbs(PoseStack poseStack, Vec3 camera) {
        if (!Settings.ENABLED || !Settings.RENDER_CRUMBS || trackedEntities.isEmpty()) return;

        // 1. Setup RenderSystem States
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(CoreShaders.POSITION_COLOR); // Using the updated shader!

        // ---> APPLY LINE WIDTH HERE <---
        RenderSystem.lineWidth(Settings.LINE_WIDTH);

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

        Matrix4f matrix = poseStack.last().pose();

        // 2. Build the vertices
        for (TrackedEntity trackedEntity : trackedEntities.values()) {
            if (trackedEntity.crumbs.size() < 2) continue;

            int color = trackedEntity.getColor();
            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;

            for (int i = 0; i < trackedEntity.crumbs.size() - 1; i++) {
                Crumb current = trackedEntity.crumbs.get(i);
                Crumb next = trackedEntity.crumbs.get(i + 1);

                float x1 = (float) (current.position().x - camera.x);
                float y1 = (float) (current.position().y - camera.y);
                float z1 = (float) (current.position().z - camera.z);

                float x2 = (float) (next.position().x - camera.x);
                float y2 = (float) (next.position().y - camera.y);
                float z2 = (float) (next.position().z - camera.z);

                buffer.addVertex(matrix, x1, y1, z1).setColor(r, g, b, 255);
                buffer.addVertex(matrix, x2, y2, z2).setColor(r, g, b, 255);
            }
        }

        // 3. Draw the buffer
        MeshData mesh = buffer.build();
        if (mesh != null) {
            BufferUploader.drawWithShader(mesh);
        }

        // 4. Cleanup RenderSystem States
        // ---> RESET LINE WIDTH HERE <---
        RenderSystem.lineWidth(1.0f);

        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
}
