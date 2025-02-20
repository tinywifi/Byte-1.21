package com.syuto.bytes.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.syuto.bytes.auth.MainMenu;
import com.syuto.bytes.module.ModuleManager;
import com.syuto.bytes.module.impl.player.FastPlace;
import dev.blend.ThemeHandler;
import dev.blend.util.render.DrawUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Function;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Inject(
            method = "<init>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/client/MinecraftClient;)Lnet/minecraft/client/gui/hud/InGameHud;",
                    shift = At.Shift.AFTER
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

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gl/Framebuffer;beginWrite(Z)V"
            )
    )
    private void updateThemeHandler(boolean tick, CallbackInfo ci) {
        ThemeHandler.INSTANCE.update();
    }

    // WHAT IS THIS???
    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void onSetScreen(Screen screen, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (screen instanceof TitleScreen) {
            client.setScreen(new MainMenu());
            ci.cancel();
        }
        /*if (!Objects.equals(LoginGUI.rsp, "AUTHENTICATION_SUCCESS")) {
            System.out.println("hello c: " + LoginGUI.rsp);
            if (!(screen instanceof LoginGUI)) {
                client.setScreen(new LoginGUI());
                ci.cancel();
            }
        }
        System.out.println("hello: " + LoginGUI.rsp);*/
    }
}
