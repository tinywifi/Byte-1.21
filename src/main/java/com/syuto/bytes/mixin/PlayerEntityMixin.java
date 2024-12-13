package com.syuto.bytes.mixin;


import com.syuto.bytes.module.ModuleManager;
import com.syuto.bytes.module.impl.combat.KeepSprint;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static com.syuto.bytes.Byte.mc;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @ModifyArgs(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setSprinting(Z)V"))
    private void modifySprintingArgs(Args args) {
        KeepSprint ks = ModuleManager.getModule(KeepSprint.class);
        if (ks != null && ks.isEnabled()) {
            args.set(0, true);
        }
    }

    @ModifyArgs(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
    private void modifyVelocityArgs(Args args) {
        KeepSprint ks = ModuleManager.getModule(KeepSprint.class);
        if (ks != null && ks.isEnabled()) {
            args.set(0, mc.player.getVelocity());
        }
    }


}
