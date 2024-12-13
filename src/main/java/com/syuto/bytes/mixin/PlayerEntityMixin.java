package com.syuto.bytes.mixin;


import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.ModuleManager;
import com.syuto.bytes.module.impl.combat.KeepSprint;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @WrapWithCondition(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
    private boolean setVelocity(PlayerEntity instance, Vec3d vec3d) {
        KeepSprint e = ModuleManager.getModule(KeepSprint.class);
        return e != null && e.isEnabled();
    }

    @WrapWithCondition(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setSprinting(Z)V"))
    private boolean setSprinting(PlayerEntity instance, boolean b) {
        KeepSprint e = ModuleManager.getModule(KeepSprint.class);
        return e != null && e.isEnabled();
    }
}
