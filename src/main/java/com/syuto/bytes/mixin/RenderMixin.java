package com.syuto.bytes.mixin;

import com.syuto.bytes.Byte;
import com.syuto.bytes.eventbus.impl.RenderTickEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class RenderMixin {

    @Inject(at = @At("TAIL"), method = "render")
    void renderTick(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        RenderTickEvent e = new RenderTickEvent(tickCounter.getTickDelta(true), context);
        Byte.INSTANCE.eventBus.post(e);
    }
}
