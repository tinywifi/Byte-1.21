package com.syuto.bytes.mixin;

import com.syuto.bytes.module.ModuleManager;
import com.syuto.bytes.module.impl.misc.MemoryCorruption;
import com.syuto.bytes.utils.impl.ReflectionUtil;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.ThreadLocalRandom;

@Mixin(MinecraftClient.class)
public class MinecraftMixin {
    @Inject(at = @At("HEAD"), method = "tick")
    private void onTick(CallbackInfo ci) {
        if(ModuleManager.getModule(MemoryCorruption.class).isEnabled()) {
            long freeAddr = ReflectionUtil.theUnsafe.allocateMemory(1); // Find the endpoint of addresses
            ReflectionUtil.theUnsafe.freeMemory(freeAddr); // Don't leak
            // This will probably crash you almost instantly :3
            ReflectionUtil.theUnsafe.setMemory(
                    freeAddr-10 + ThreadLocalRandom.current().nextLong(-10, 11),
                    ThreadLocalRandom.current().nextLong(1, 11),
                    (byte) 0
            );
        }
    }
}
