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
import net.minecraft.item.consume.UseAction;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static com.syuto.bytes.Byte.mc;


@Mixin(HeldItemRenderer.class)
public abstract class ItemRendererMixin {


    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
    private void hideShield(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        Animations mod = ModuleManager.getModule(Animations.class);
        assert mod != null;
        if (mod.isEnabled() && hand == Hand.OFF_HAND) {
            ci.cancel();
        }
    }

    @Redirect(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getUseAction()Lnet/minecraft/item/consume/UseAction;", ordinal = 0))
    private UseAction hookUseAction(ItemStack instance) {
        var item = instance.getItem();

        Animations mod = ModuleManager.getModule(Animations.class);
        assert mod != null;
        if (mod.isEnabled() && mc.player.isUsingItem() && item instanceof SwordItem) {
            return UseAction.BLOCK;
        }

        return instance.getUseAction();
    }


    @ModifyArg(method = "renderFirstPersonItem", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
            ordinal = 3
    ), index = 2)
    private float applyEquipOffset(float equipProgress) {
        Animations mod = ModuleManager.getModule(Animations.class);
        assert mod != null;
        if (mod.isEnabled() && mc.player.isUsingItem()) {
            return 0.0F;
        }

        return equipProgress;
    }

    @Redirect(method = "renderFirstPersonItem", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getActiveHand()Lnet/minecraft/util/Hand;",
            ordinal = 1
    ))
    private Hand hookActiveHand(AbstractClientPlayerEntity instance) {
        var item = instance.getMainHandStack().getItem();

        Animations mod = ModuleManager.getModule(Animations.class);
        assert mod != null;
        if (mod.isEnabled() && mc.player.isUsingItem() && item instanceof SwordItem) {
            return Hand.MAIN_HAND;
        }

        return instance.getActiveHand();
    }



    @Inject(method = "renderFirstPersonItem", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V",
            ordinal = 5
        )
    )
    public void doAnimatiosn(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        matrices.translate(0.1f, AnimationUtils.height, -0.1f);
    }


    @Inject(method = "renderFirstPersonItem", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V",
            ordinal = 7
        )
    )
    public void doAnimation(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        AnimationUtils.animate(matrices, swingProgress);
    }



}
