package me.day.cannoningutilities.gizmos;

import me.day.cannoningutilities.config.Settings;
import net.minecraft.gizmos.Gizmo;
import net.minecraft.gizmos.GizmoPrimitives;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public record BoxOutlineGizmo(AABB box, int color) implements Gizmo {
    @Override
    public void emit(@NonNull GizmoPrimitives gizmo, float factor) {
        int finalColor = ARGB.multiplyAlpha(color, factor);

        Vec3[] corners = {new Vec3(box.minX, box.minY, box.minZ), new Vec3(box.maxX, box.minY, box.minZ), new Vec3(box.maxX, box.minY, box.maxZ), new Vec3(box.minX, box.minY, box.maxZ), new Vec3(box.minX, box.maxY, box.minZ), new Vec3(box.maxX, box.maxY, box.minZ), new Vec3(box.maxX, box.maxY, box.maxZ), new Vec3(box.minX, box.maxY, box.maxZ)};

        for (int i = 0; i < 4; i++)
            gizmo.addLine(corners[i], corners[(i + 1) % 4], finalColor, Settings.LINE_WIDTH);

        for (int i = 4; i < 8; i++)
            gizmo.addLine(corners[i], corners[4 + ((i + 1) % 4)], finalColor, Settings.LINE_WIDTH);

        for (int i = 0; i < 4; i++)
            gizmo.addLine(corners[i], corners[i + 4], finalColor, Settings.LINE_WIDTH);
    }
}
