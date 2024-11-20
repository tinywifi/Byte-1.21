package com.syuto.bytes.utils.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.RotationAxis;

import static com.syuto.bytes.Byte.mc;

public class RenderUtils {

    public static void renderEntityBox(Entity e, MatrixStack matrices) {
        float outlineThickness = 1.0f;
        double expand = 0;
        int color = 0;

        if (e instanceof LivingEntity) {

            double x = e.lastRenderX + (e.getX() - e.lastRenderX) * mc.getRenderTickCounter().getTickDelta(false) - mc.gameRenderer.getCamera().getPos().x;
            double y = e.lastRenderY + (e.getY() - e.lastRenderY) *  mc.getRenderTickCounter().getTickDelta(false) - mc.gameRenderer.getCamera().getPos().y;
            double z = e.lastRenderZ + (e.getZ() - e.lastRenderZ) *  mc.getRenderTickCounter().getTickDelta(false) - mc.gameRenderer.getCamera().getPos().z;

            float d = (float) expand / 40.0F;

            // Set the color based on the entity type
            if (e instanceof PlayerEntity) {
                color = 0xFF0000; // Red
            } else {
                color = 0xFFFFFF; // White
            }

            matrices.push();
            matrices.translate(x, y - 0.2D, z);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-mc.gameRenderer.getCamera().getYaw()));
            matrices.scale(0.03F + d, 0.03F + d, 0.03F + d);

            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            //RenderSystem.setShader();

            // Draw the lines of the box
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR);


            drawLine(buffer, -20, 0, 0, 21, 0, 0, color);
            drawLine(buffer, -20, 75, 0, 21, 75, 0, color);
            drawLine(buffer, -20, 0, 0, -20, 75, 0, color);
            drawLine(buffer, 21, 0, 0, 21, 75, 0, color);
            tessellator.clear();

            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();

            matrices.pop();
        }
    }

    private static void drawLine(BufferBuilder buffer, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
        float a = (color >> 24 & 255) / 255.0F;
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;

        buffer.vertex((float) x1, (float) y1, (float) z1).color(r, g, b, a);
        buffer.vertex((float) x2, (float) y2, (float) z2).color(r, g, b, a);
    }

}
