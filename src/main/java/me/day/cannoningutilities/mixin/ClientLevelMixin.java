package me.day.cannoningutilities.mixin;

import me.day.cannoningutilities.config.Settings;
import me.day.cannoningutilities.core.Rendering;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {

    @Inject(at = @At("HEAD"), method = "addEntity", cancellable = true)
    private void addEntity(Entity entity, CallbackInfo ci) {
        if (!Settings.ENABLED) return;
        if (Settings.TNT_RENDERING != Rendering.REMOVE_ENTITY_LIST) return;
        if (entity instanceof PrimedTnt || entity instanceof FallingBlockEntity) ci.cancel();
    }
}
