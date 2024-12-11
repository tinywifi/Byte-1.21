package com.syuto.bytes.mixin;

import com.syuto.bytes.eventbus.impl.PreMotionEvent;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.syuto.bytes.Byte.mc;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    @Inject(method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V", at = @At("RETURN"))
    private void updateRenderStateRotations(AbstractClientPlayerEntity player, PlayerEntityRenderState state, float tickDelta, CallbackInfo info) {
        if (player == mc.player) {
            state.pitch = MathHelper.clamp(getLerpedPitch(tickDelta), -90.0f, 90.0f);
        }
    }


    @Unique
    public float getLerpedPitch(float tickDelta) {
        return tickDelta == 1.0F ? PreMotionEvent.pitch : MathHelper.lerp(tickDelta, PreMotionEvent.lastPitch, PreMotionEvent.pitch);
    }

    @Unique
    public float getLerpedYaw(float tickDelta) {
        return tickDelta == 1.0F ? mc.player.headYaw : MathHelper.lerp(tickDelta, mc.player.prevHeadYaw, mc.player.headYaw);
    }
}
