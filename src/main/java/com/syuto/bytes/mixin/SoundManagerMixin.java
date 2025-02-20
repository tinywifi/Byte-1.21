package com.syuto.bytes.mixin;


import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundManager.class)
public class SoundManagerMixin {
    private static final Identifier WEAK_ATTACK_SOUND_ID = Identifier.of("minecraft", "entity.player.attack.weak");

    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At("HEAD"), cancellable = true)
    private void cancelWeakAttackSound(SoundInstance soundInstance, CallbackInfo ci) {
        if (soundInstance.getId().equals(WEAK_ATTACK_SOUND_ID)) {
            ci.cancel();
        }
    }
}
