package com.syuto.bytes.mixin;

import com.syuto.bytes.Byte;
import com.syuto.bytes.eventbus.impl.RenderWorldEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.syuto.bytes.Byte.mc;

@Mixin(GameRenderer.class)
public class RenderWorldMixin {

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clear(I)V"))
    void render(RenderTickCounter renderTickCounter, CallbackInfo ci) {
        float d = renderTickCounter.getTickDelta(true);
        MatrixStack stack = new MatrixStack();
        Quaternionf quaternionf = mc.gameRenderer.getCamera().getRotation().conjugate(new Quaternionf());
        Matrix4f matrix2 = (new Matrix4f()).rotation(quaternionf);
        stack.multiplyPositionMatrix(matrix2);
        Byte.INSTANCE.eventBus.post(new RenderWorldEvent(d, stack));
    }
}
