package com.syuto.bytes.mixin;


import com.syuto.bytes.eventbus.impl.PreMotionEvent;
import com.syuto.bytes.module.ModuleManager;
import com.syuto.bytes.module.impl.player.Scaffold;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static com.syuto.bytes.Byte.mc;


@Mixin(Entity.class)
public class EntityMixin {


    @ModifyArgs(method = "pushAwayFrom(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    private void onPushAwayFrom(Args args, Entity entity) {
        Scaffold test = ModuleManager.getModule(Scaffold.class);
        if (test != null && test.isEnabled()) {
            if ((Object) this == mc.player) {
                args.set(0, (double) args.get(0) * 0);
                args.set(2, (double) args.get(2) * 0);
            }
        }
    }

}
