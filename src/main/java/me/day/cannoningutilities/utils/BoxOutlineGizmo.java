package me.day.cannoningutilities.utils;

import net.minecraft.gizmos.Gizmo;
import net.minecraft.gizmos.GizmoPrimitives;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public record BoxOutlineGizmo(AABB box, int color) implements Gizmo {
    @Override
    public void emit(GizmoPrimitives gizmo, float factor) {
        int finalColor = ARGB.multiplyAlpha(color, factor);

        float minX = (float) box.minX;
        float minY = (float) box.minY;
        float minZ = (float) box.minZ;
        float maxX = (float) box.maxX;
        float maxY = (float) box.maxY;
        float maxZ = (float) box.maxZ;

        gizmo.addLine(new Vec3(minX, minY, minZ), new Vec3(maxX, minY, minZ), finalColor, 2.0F);
        gizmo.addLine(new Vec3(maxX, minY, minZ), new Vec3(maxX, minY, maxZ), finalColor, 2.0F);
        gizmo.addLine(new Vec3(maxX, minY, maxZ), new Vec3(minX, minY, maxZ), finalColor, 2.0F);
        gizmo.addLine(new Vec3(minX, minY, maxZ), new Vec3(minX, minY, minZ), finalColor, 2.0F);

        gizmo.addLine(new Vec3(minX, maxY, minZ), new Vec3(maxX, maxY, minZ), finalColor, 2.0F);
        gizmo.addLine(new Vec3(maxX, maxY, minZ), new Vec3(maxX, maxY, maxZ), finalColor, 2.0F);
        gizmo.addLine(new Vec3(maxX, maxY, maxZ), new Vec3(minX, maxY, maxZ), finalColor, 2.0F);
        gizmo.addLine(new Vec3(minX, maxY, maxZ), new Vec3(minX, maxY, minZ), finalColor, 2.0F);

        gizmo.addLine(new Vec3(minX, minY, minZ), new Vec3(minX, maxY, minZ), finalColor, 2.0F);
        gizmo.addLine(new Vec3(maxX, minY, minZ), new Vec3(maxX, maxY, minZ), finalColor, 2.0F);
        gizmo.addLine(new Vec3(maxX, minY, maxZ), new Vec3(maxX, maxY, maxZ), finalColor, 2.0F);
        gizmo.addLine(new Vec3(minX, minY, maxZ), new Vec3(minX, maxY, maxZ), finalColor, 2.0F);
    }
}
