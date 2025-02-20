package com.syuto.bytes.module.impl.render.click;

import com.mojang.blaze3d.systems.RenderSystem;
import com.syuto.bytes.module.impl.render.click.impl.Button;
import com.syuto.bytes.utils.impl.render.RenderUtils;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

import static com.syuto.bytes.Byte.mc;

public class ClickGuiScreen extends Screen {
    public ClickGuiScreen() {
        super(Text.of("byte"));
    }


    private boolean wasClicking = false;
    private boolean isActive = false;


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int width = mc.getWindow().getScaledWidth();
        int height = mc.getWindow().getScaledHeight();


        if (isOver(width / 2f, height / 2f, 50, 50, mouseX, mouseY)) {
            boolean isClicking = GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
            if (isClicking && !wasClicking) {
                isActive = !isActive;
            }
            wasClicking = isClicking;
        } else {
            wasClicking = false;
        }


        Color e = isActive ? Color.BLUE : Color.RED;

        float y = 50;

        RenderUtils.drawRect(context, 100, 50, 125, 200, e.getRGB());


        RenderUtils.drawLine(
                context,
                width / 2f - 25,
                height / 2f - 25,
                width / 2f + 25,
                height / 2f + 25,
                1f,
                Color.white.getRGB()
        );


        RenderUtils.drawLine(
                context,
                width / 2f - 25,
                height / 2f + 25,
                width / 2f + 25,
                height / 2f - 25,
                1f,
                Color.white.getRGB()
        );


    }

    public boolean isOver(double x, double y, double width, double height, double mouseX, double mouseY) {
        return mouseX > x - width / 2 &&
                mouseX < x + width / 2 &&
                mouseY > y - height / 2 &&
                mouseY < y + height / 2;
    }


    @Override
    public boolean shouldPause() {
        return false;
    }


}
