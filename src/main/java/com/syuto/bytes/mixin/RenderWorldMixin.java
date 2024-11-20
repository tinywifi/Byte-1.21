package com.syuto.bytes.mixin;

import com.syuto.bytes.Byte;
import com.syuto.bytes.eventbus.impl.RenderWorldEvent;
import net.minecraft.client.render.*;
import net.minecraft.client.util.ObjectAllocator;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class RenderWorldMixin {


    @Shadow private int ticks;

    @Inject(at = @At("TAIL"), method = "renderWorld")
    void render(RenderTickCounter renderTickCounter, CallbackInfo ci) {
        Byte.INSTANCE.eventBus.post(new RenderWorldEvent(renderTickCounter.getTickDelta(false)));

    }

}
