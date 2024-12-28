package com.syuto.bytes.mixin;


import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.TextVisitFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static com.syuto.bytes.Byte.mc;

@Mixin(TextVisitFactory.class)
public class TextRendererMixin {
    @ModifyArg(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/text/TextVisitFactory;visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z",
                    ordinal = 0
            ),
            method = "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z",
            index = 0
    )
    private static String adjustText(String text) {
        if (mc.player != null) {
            String playerName = mc.player.getName().getLiteralString();
            text = text.replaceAll(playerName, "Byte");
        }
        return text;
    }
}
