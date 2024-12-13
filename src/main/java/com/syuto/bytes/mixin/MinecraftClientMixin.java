package com.syuto.bytes.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.syuto.bytes.module.ModuleManager;
import com.syuto.bytes.module.impl.player.FastPlace;
import com.syuto.bytes.module.impl.render.clickgui.ImGuiImpl;
import dev.blend.util.render.DrawUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow
    @Final
    private Window window;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void initImGui(RunArgs args, CallbackInfo ci) {
        ImGuiImpl.create(window.getHandle());
    }

    @Inject(
            method = "<init>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/render/item/HeldItemRenderer;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/client/render/BufferBuilderStorage;)Lnet/minecraft/client/render/GameRenderer;"
            )
    )
    private void initializeNanoVG(RunArgs args, CallbackInfo ci) {
        DrawUtil.initialize();
    }

    @Shadow
    private int itemUseCooldown;


    @Shadow protected abstract void createInitScreens(List<Function<Runnable, Screen>> list);

    @Inject(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isItemEnabled(Lnet/minecraft/resource/featuretoggle/FeatureSet;)Z"))
    private void onDoItemUseHand(CallbackInfo ci, @Local ItemStack itemStack) {
        final FastPlace fastPlace = ModuleManager.getModule(FastPlace.class);
        if (fastPlace != null && fastPlace.isEnabled()) {
            itemUseCooldown = fastPlace.getItemUseCooldown(itemStack);
        }
    }

    // WHAT IS THIS???
    /*@Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void onSetScreen(Screen screen, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!Objects.equals(LoginGUI.rsp, "AUTHENTICATION_SUCCESS")) {
            System.out.println("hello c: " + LoginGUI.rsp);
            if (!(screen instanceof LoginGUI)) {
                client.setScreen(new LoginGUI());
                ci.cancel();
            }
        }
        System.out.println("hello: " + LoginGUI.rsp);
    }*/
}
