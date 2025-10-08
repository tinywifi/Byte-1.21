package com.syuto.bytes.mixin;

import com.syuto.bytes.utils.impl.rotation.MixinUtils;
import com.syuto.bytes.utils.impl.rotation.RotationUtils;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.syuto.bytes.Byte.mc;


@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<
        T extends LivingEntity,
        S extends LivingEntityRenderState> {


    @Inject(
            method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;getCustomName()Lnet/minecraft/text/Text;",
                    shift = At.Shift.BEFORE
            )
    )
    public void updateRenderState(T livingEntity, S livingEntityRenderState, float f, CallbackInfo ci) {
        if (livingEntity == mc.player) {

            if (RotationUtils.yawChanged) {
                float g = MathHelper.lerpAngleDegrees(
                        f,
                        RotationUtils.getLastRotationYaw(),
                        RotationUtils.getRotationYaw()
                );

                MixinUtils.turnHead(g, livingEntityRenderState);

            }

            livingEntityRenderState.pitch = MixinUtils.getLerpedPitch(f, livingEntity);
        }
    }

}
