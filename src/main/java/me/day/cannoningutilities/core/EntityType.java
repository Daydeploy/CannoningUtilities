package me.day.cannoningutilities.core;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.block.Blocks;

public enum EntityType {
    TNT(0xFF0000), SAND(0xFFFF00), RED_SAND(0xFF00FF), GRAVEL(0x00FFFF), ANVIL(0x00FF00), OTHER(0xFFFF00);

    public final int color;

    EntityType(int color) {
        this.color = color;
    }

    public static EntityType fromEntity(Entity entity) {
        return switch (entity) {
            case PrimedTnt t -> TNT;
            case FallingBlockEntity falling -> {
                var block = falling.getBlockState().getBlock();
                if (block == Blocks.SAND) yield SAND;
                else if (block == Blocks.RED_SAND) yield RED_SAND;
                else if (block == Blocks.GRAVEL) yield GRAVEL;
                else if (block == Blocks.ANVIL) yield ANVIL;
                else yield OTHER;
            }
            default -> OTHER;
        };
    }
}
