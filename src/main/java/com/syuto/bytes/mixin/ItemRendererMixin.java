package com.syuto.bytes.mixin;

import com.syuto.bytes.module.ModuleManager;
import com.syuto.bytes.module.impl.render.Animations;
import com.syuto.bytes.utils.impl.render.AnimationUtils;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.syuto.bytes.Byte.mc;

@Mixin(HeldItemRenderer.class)
public abstract class ItemRendererMixin {

    @Inject(method = "renderFirstPersonItem", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V",
            ordinal = 4,
            shift = At.Shift.BEFORE
        )
    )
    public void doAnimation(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        float f =  MathHelper.sin((float) (swingProgress * swingProgress * Math.PI));
        Animations animation = ModuleManager.getModule(Animations.class);
        assert animation != null;

        if (animation.isEnabled()) {
            AnimationUtils.animate(matrices, player.getHandSwingProgress(tickDelta), f);
        }
    }



    @Redirect(method = "renderFirstPersonItem", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isUsingItem()Z",
            ordinal = 1
    ))
    private boolean hookIsUseItem(AbstractClientPlayerEntity instance) {
        var item = instance.getMainHandStack().getItem();
        if (AnimationUtils.isBlocking() && item instanceof SwordItem) {
            return true;
        }
        return instance.isUsingItem();
    }


    @Redirect(method = "renderFirstPersonItem", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getItemUseTimeLeft()I",
            ordinal = 2
    ))
    private int hookItemUseItem(AbstractClientPlayerEntity instance) {
        var item = instance.getMainHandStack().getItem();
        if (AnimationUtils.isBlocking() && item instanceof SwordItem) {
            return 7200;
        }
        return instance.getItemUseTimeLeft();
    }

}
