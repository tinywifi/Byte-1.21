package com.syuto.bytes.auth;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import static com.syuto.bytes.Byte.mc;

public class TransparentButton extends ButtonWidget {
    public TransparentButton(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION_SUPPLIER);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        int alpha = isHovered() ? 160 : 120;
        int color = (alpha << 24) | 0x000000;
        context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), color);
        int textColor = isHovered() ? 0xFFFFFF : 0xE5E5E5;
        context.drawTextWithShadow(
                mc.textRenderer,
                getMessage(),
                getX() + getWidth() / 2 - mc.textRenderer.getWidth(getMessage()) / 2,
                getY() + (getHeight() - 8) / 2,
                textColor);
    }
}