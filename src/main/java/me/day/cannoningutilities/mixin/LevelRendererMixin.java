package me.day.cannoningutilities.mixin;

import me.day.cannoningutilities.config.Settings;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Unique
    private final Set<BlockPos> renderedTntPositions = new HashSet<>();

    @Inject(at = @At("HEAD"), method = "renderLevel")
    private void clearPositions(CallbackInfo ci) {
        renderedTntPositions.clear();
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;shouldRender(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/culling/Frustum;DDD)Z"), method = "extractVisibleEntities")
    private boolean onShouldRender(EntityRenderDispatcher dispatcher, Entity entity, Frustum frustum, double x, double y, double z) {
        if (Settings.ENABLED) {
            if (entity instanceof PrimedTnt || entity instanceof FallingBlockEntity) {
                return switch (Settings.TNT_RENDERING) {
                    case ALL_RENDERING -> false;
                    case SAME_BLOCK_RENDERING -> renderedTntPositions.add(entity.blockPosition());
                    default -> dispatcher.shouldRender(entity, frustum, x, y, z);
                };
            }
        }
        return dispatcher.shouldRender(entity, frustum, x, y, z);
    }
}
