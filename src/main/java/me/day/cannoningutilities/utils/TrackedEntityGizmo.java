package me.day.cannoningutilities.utils;

import net.minecraft.gizmos.Gizmo;
import net.minecraft.gizmos.GizmoPrimitives;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public record TrackedEntityGizmo(TrackedEntity entity) implements Gizmo {

    @Override
    public void emit(@NonNull GizmoPrimitives gizmo, float factor) {
        if (entity.crumbs.size() < 2) return;

        int argb = 0xFF000000 | entity.getColor();
        int color = ARGB.multiplyAlpha(argb, factor);

        for (int i = 0; i < entity.crumbs.size() - 1; i++) {
            Vec3 start = entity.crumbs.get(i).position();
            Vec3 end = entity.crumbs.get(i + 1).position();

            gizmo.addLine(start, end, color, 2.0F);
        }
    }
}
