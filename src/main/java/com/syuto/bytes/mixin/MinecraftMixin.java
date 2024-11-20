package com.syuto.bytes.mixin;

import com.syuto.bytes.Byte;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftMixin {
    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/Window;setPhase(Ljava/lang/String;)V",
                    args = "ldc=\"Post startup\""
            )
    )
    private void onLoad(CallbackInfo info) {
        Byte.INSTANCE.onInitialize();
    }
}
