package com.syuto.bytes.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.syuto.bytes.modules.impl.player.FastPlace;
import me.twenty48lol.util.render.DrawUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(
            method = "<init>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/render/item/HeldItemRenderer;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/client/render/BufferBuilderStorage;)Lnet/minecraft/client/render/GameRenderer;"
            )
    )
    private void initializeNanoVG(RunArgs args, CallbackInfo ci) {
        DrawUtil.initializeNanoVG();
    }



    @Shadow
    private int itemUseCooldown;

    @Inject(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isItemEnabled(Lnet/minecraft/resource/featuretoggle/FeatureSet;)Z"))
    private void onDoItemUseHand(CallbackInfo ci, @Local ItemStack itemStack) {
        itemUseCooldown = FastPlace.getItemUseCooldown(itemStack);
    }





}
