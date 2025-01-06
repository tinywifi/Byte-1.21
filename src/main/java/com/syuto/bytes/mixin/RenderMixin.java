package com.syuto.bytes.mixin;

import com.syuto.bytes.Byte;
import com.syuto.bytes.eventbus.impl.PreMotionEvent;
import com.syuto.bytes.eventbus.impl.RenderTickEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.syuto.bytes.Byte.mc;


@Mixin(InGameHud.class)
public abstract class RenderMixin {

    @Shadow private int ticks;

    @Shadow public abstract int getTicks();

    @Inject(at = @At("HEAD"), method = "renderMainHud")
    void renderTick(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        RenderTickEvent e = new RenderTickEvent(tickCounter.getTickDelta(true), context);
        Byte.INSTANCE.eventBus.post(e);
    }

    @Inject(at = @At("HEAD"), method = "renderHeldItemTooltip")
    void renderTicker(DrawContext context, CallbackInfo ci) {
        RenderTickEvent e = new RenderTickEvent(mc.getRenderTickCounter().getTickDelta(true), context);
        Byte.INSTANCE.eventBus.post(e);
    }

}
