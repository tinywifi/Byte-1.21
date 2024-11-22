package com.syuto.bytes.mixin;

import com.syuto.bytes.Byte;
import com.syuto.bytes.eventbus.impl.KeyEvent;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Inject(
            method = "onKey",
            at = @At(
                    value = "HEAD"
            )
    )
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (
                MinecraftClient.getInstance().getWindow().getHandle() == window
                && MinecraftClient.getInstance().player != null
                && MinecraftClient.getInstance().currentScreen == null
        ) {
            final KeyEvent event = new KeyEvent(key, scancode, action, modifiers);
            Byte.INSTANCE.eventBus.post(event);
            Byte.INSTANCE.handlers.onKey(event);
        }
    }

}
