package me.day.cannoningutilities.utils;

import me.day.cannoningutilities.config.Settings;
import me.day.cannoningutilities.core.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TrackedEntity {
    public final UUID uuid;
    public final EntityType type;
    public final List<Crumb> crumbs;
    public final long expiryTime;
    public final Vec3 startPosition;

    public TrackedEntity(Entity entity) {
        this.uuid = entity.getUUID();
        this.type = EntityType.fromEntity(entity);
        this.crumbs = Collections.synchronizedList(new ArrayList<>());
        this.expiryTime = System.currentTimeMillis() + (Settings.REMOVAL_TIME_SECONDS * 1000L);
        this.startPosition = entity.position();
    }

    public void addCrumb(Vec3 position) {
        crumbs.add(new Crumb(position, System.nanoTime()));
    }

    public boolean isExpired() {
        return Settings.REMOVAL_TIME_SECONDS > 0 && System.currentTimeMillis() > expiryTime;
    }

    public int getColor() {
        return type.color;
    }
}
