package com.syuto.bytes.mixin;


import com.syuto.bytes.module.ModuleManager;
import com.syuto.bytes.module.impl.combat.Killaura;
import com.syuto.bytes.module.impl.misc.Test;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
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
    private void updateRenderStateRotations(AbstractClientPlayerEntity player, PlayerEntityRenderState state, float tickDelta, CallbackInfo info) {
        final Killaura ka = ModuleManager.getModule(Killaura.class);
        if (ka != null && ka.rots != null && ka.isEnabled() && player == mc.player) {
            float yaw = Killaura.rots[0];
            float pitch = Killaura.rots[1];
            state.bodyYaw = MathHelper.lerp(tickDelta, lastBodyYaw, yaw);
            state.yawDegrees = yaw;
            state.pitch = MathHelper.lerp(tickDelta, lastPitch, pitch);


            lastBodyYaw = state.bodyYaw;
            lastPitch = state.pitch;

        }
    }
}
