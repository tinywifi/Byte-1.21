package com.syuto.bytes.mixin;


import com.syuto.bytes.module.ModuleManager;
import com.syuto.bytes.module.impl.misc.Test;
import com.syuto.bytes.module.impl.player.FastPlace;
import com.syuto.bytes.utils.impl.ChatUtils;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.syuto.bytes.Byte.mc;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    private float lastBodyYaw;
    private float lastPitch;

    @Inject(method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V", at = @At("RETURN"))
    private void updateRenderStateRotations(AbstractClientPlayerEntity player, PlayerEntityRenderState state, float f, CallbackInfo info) {
        final Test test = ModuleManager.getModule(Test.class);
        if (test != null && Test.rots != null && test.isEnabled() && player == mc.player) {
            float tickDelta = mc.getRenderTickCounter().getTickDelta(false);
            state.bodyYaw = MathHelper.lerpAngleDegrees(f, lastBodyYaw, Test.rots[0]);
            state.yawDegrees = MathHelper.wrapDegrees(state.bodyYaw - lastBodyYaw);
            state.pitch = MathHelper.lerp(f, lastPitch, Test.rots[1]);
            System.out.println("hi " + state.pitch + " " + state.bodyYaw);
            lastBodyYaw = state.bodyYaw;
            lastPitch = state.pitch;
        }
    }

    private static float interpolateRotation(float start, float end, float fraction) {
        float delta = end - start;
        while (delta < -180.0F) delta += 360.0F;
        while (delta >= 180.0F) delta -= 360.0F;
        return start + fraction * delta;
    }
}
