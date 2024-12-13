package com.syuto.bytes.mixin;

import com.syuto.bytes.module.ModuleManager;
import com.syuto.bytes.module.impl.render.Animations;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.syuto.bytes.Byte.mc;


@Mixin(HeldItemRenderer.class)
public abstract class ItemRendererMixin {

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
    private void hideShield(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        Animations mod = ModuleManager.getModule(Animations.class);
        assert mod != null;
        if (mod.isEnabled()  && mc.player.isUsingItem() &&  hand == Hand.OFF_HAND) {
            ci.cancel();
        }
    }



    @Redirect(
            method = "renderFirstPersonItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;getUseAction()Lnet/minecraft/item/consume/UseAction;",
                    ordinal = 0
            )
    )
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
            ordinal = 4
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
            target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isUsingItem()Z",
            ordinal = 1
    ))
    private boolean hookIsUseItem(AbstractClientPlayerEntity instance) {
        var item = instance.getMainHandStack().getItem();

        Animations mod = ModuleManager.getModule(Animations.class);
        assert mod != null;
        if (mod.isEnabled() && mc.player.isUsingItem() && item instanceof SwordItem) {
            return true;
        }

        return instance.isUsingItem();
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


    @Redirect(method = "renderFirstPersonItem", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getItemUseTimeLeft()I",
            ordinal = 2
    ))
    private int hookItemUseItem(AbstractClientPlayerEntity instance) {
        var item = instance.getMainHandStack().getItem();

        Animations mod = ModuleManager.getModule(Animations.class);
        assert mod != null;
        if (mod.isEnabled() && mc.player.isUsingItem() && item instanceof SwordItem) {
            return 7200;
        }

        return instance.getItemUseTimeLeft();
    }

    @Inject(method = "renderFirstPersonItem",
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getUseAction()Lnet/minecraft/item/consume/UseAction;")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", ordinal = 2, shift = At.Shift.AFTER))
    private void transformLegacyBlockAnimations(AbstractClientPlayerEntity player, float tickDelta, float pitch,
                                                Hand hand, float swingProgress, ItemStack item, float equipProgress,
                                                MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                                                CallbackInfo ci) {

        Animations mod = ModuleManager.getModule(Animations.class);
        assert mod != null;
        if (mod.isEnabled() && mc.player.isUsingItem() && item.getItem() instanceof SwordItem) {
            final Arm arm = (hand == Hand.MAIN_HAND) ? player.getMainArm() : player.getMainArm().getOpposite();

            transform(matrices, arm, equipProgress, swingProgress);
            return;
        }
    }

    public void transform(MatrixStack matrices, Arm arm, float equipProgress, float swingProgress) {
        float translateY = (arm == Arm.RIGHT) ? -0.1f : 0.1f;
        matrices.translate(translateY, 0.1f, 0.0f);
        applySwingOffset(matrices, arm, swingProgress * 0.9f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-102.25f));
        matrices.multiply((arm == Arm.RIGHT ? RotationAxis.POSITIVE_Y : RotationAxis.NEGATIVE_Y).rotationDegrees(13.365f));
        matrices.multiply((arm == Arm.RIGHT ? RotationAxis.POSITIVE_Z : RotationAxis.NEGATIVE_Z).rotationDegrees(78.05f));
    }


    protected void applySwingOffset(MatrixStack matrices, Arm arm, float swingProgress) {
        int armSide = (arm == Arm.RIGHT) ? 1 : -1;

        float f = (float) Math.sin(swingProgress * swingProgress * Math.PI);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(armSide * (45.0f + f * -20.0f)));
        float g = (float) Math.sin(MathHelper.sqrt(swingProgress) * Math.PI);


        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(armSide * g * -20.0f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(g * -80.0f));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(armSide * -45.0f));
    }



}
