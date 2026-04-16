package me.day.cannoningutilities.mixin;

import me.day.cannoningutilities.config.Settings;
import me.day.cannoningutilities.core.Rendering;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
//    @Inject(at = @At("HEAD"), method = "tickEntities")
//    private void onTickEntities(CallbackInfo ci) {
//        if (!Settings.ENABLED || Settings.TNT_RENDERING == Rendering.ENABLED || Settings.TNT_RENDERING == Rendering.ALL_RENDERING)
//            return;
//
//        ClientLevel level = (ClientLevel) (Object) this;
//
//        if (Settings.TNT_RENDERING == Rendering.REMOVE_ENTITY_LIST) {
//            for (Entity entity : level.entitiesForRendering()) {
//                if (entity instanceof PrimedTnt || entity instanceof FallingBlockEntity) entity.discard();
//            }
//        } else if (Settings.TNT_RENDERING == Rendering.SAME_BLOCK_RENDERING) {
//            Set<BlockPos> positions = new HashSet<>();
//            for (Entity entity : level.entitiesForRendering()) {
//                if (entity instanceof PrimedTnt || entity instanceof FallingBlockEntity) {
//                    BlockPos pos = entity.blockPosition();
//                    if (positions.contains(pos)) {
//                        entity.discard();
//                    } else {
//                        positions.add(pos);
//                    }
//                }
//            }
//        }
//    }

    @Inject(at = @At("HEAD"), method = "addEntity", cancellable = true)
    private void addEntity(Entity entity, CallbackInfo ci) {
        if (!Settings.ENABLED) return;
        if (Settings.TNT_RENDERING != Rendering.REMOVE_ENTITY_LIST) return;
        if (entity instanceof PrimedTnt || entity instanceof FallingBlockEntity) ci.cancel();
    }
}
