package com.syuto.bytes.mixin;


import com.syuto.bytes.module.ModuleManager;
import com.syuto.bytes.module.impl.misc.Test;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static com.syuto.bytes.Byte.mc;


@Mixin(Entity.class)
public class EntityMixin {


    @ModifyArgs(method = "pushAwayFrom(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    private void onPushAwayFrom(Args args, Entity entity) {
        Test test = ModuleManager.getModule(Test.class);
        if (test != null && test.isEnabled()) {
            if ((Object) this == mc.player) {
                args.set(0, (double) args.get(0) * 0);
                args.set(2, (double) args.get(2) * 0);
            }
        }
    }
}
