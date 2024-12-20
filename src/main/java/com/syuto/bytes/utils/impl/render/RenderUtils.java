package com.syuto.bytes.utils.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.syuto.bytes.eventbus.impl.RenderTickEvent;
import com.syuto.bytes.eventbus.impl.RenderWorldEvent;
import com.syuto.bytes.utils.impl.client.ChatUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.datafixer.fix.ChunkPalettedStorageFix;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

import static com.syuto.bytes.Byte.mc;

public class RenderUtils {
    private static final Color black = Color.black;
    private static final Color gray = Color.darkGray;
    private static final Color green = Color.green;
    private static final Color yellow = Color.yellow;
    private static final Color red = Color.red;
    private static final Color orange = Color.orange;
    private static final Color white = Color.white;

    public static void renderBox(Entity e, RenderWorldEvent event, float delta) {
        float interpolatedX = (float) (e.lastRenderX + (e.getX() - e.lastRenderX) * delta - mc.gameRenderer.getCamera().getPos().x);
        float interpolatedY = (float) (e.lastRenderY + (e.getY() - e.lastRenderY) * delta - mc.gameRenderer.getCamera().getPos().y);
        float interpolatedZ = (float) (e.lastRenderZ + (e.getZ() - e.lastRenderZ) * delta - mc.gameRenderer.getCamera().getPos().z);

        Box box = e.getBoundingBox();

        float minX = (float) (box.minX - e.getX()) - 0.12f;
        float maxX = (float) (box.maxX - e.getX()) + 0.12f;
        float minY = (float) (box.minY - e.getY()) - 0.12f;
        float maxY = (float) (box.maxY - e.getY()) + 0.12f;
        float minZ = (float) (box.minZ - e.getZ()) - 0.12f;
        float maxZ = (float) (box.maxZ - e.getZ()) + 0.12f;

        BufferBuilder vb = getBufferBuilder(event.matrixStack, VertexFormat.DrawMode.QUADS);
        preRender();
        MatrixStack matrixStack = event.matrixStack;
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();

        matrixStack.translate(interpolatedX, interpolatedY, interpolatedZ);

        int r = 255, g = 255, b = 255, a = 75;

        vb.vertex(matrix, minX, minY, minZ).color(r, g, b, a);
        vb.vertex(matrix, maxX, minY, minZ).color(r, g, b, a);
        vb.vertex(matrix, maxX, minY, maxZ).color(r, g, b, a);
        vb.vertex(matrix, minX, minY, maxZ).color(r, g, b, a);

        vb.vertex(matrix, minX, maxY, minZ).color(r, g, b, a);
        vb.vertex(matrix, maxX, maxY, minZ).color(r, g, b, a);
        vb.vertex(matrix, maxX, maxY, maxZ).color(r, g, b, a);
        vb.vertex(matrix, minX, maxY, maxZ).color(r, g, b, a);

        vb.vertex(matrix, minX, minY, minZ).color(r, g, b, a);
        vb.vertex(matrix, maxX, minY, minZ).color(r, g, b, a);
        vb.vertex(matrix, maxX, maxY, minZ).color(r, g, b, a);
        vb.vertex(matrix, minX, maxY, minZ).color(r, g, b, a);

        vb.vertex(matrix, minX, minY, maxZ).color(r, g, b, a);
        vb.vertex(matrix, maxX, minY, maxZ).color(r, g, b, a);
        vb.vertex(matrix, maxX, maxY, maxZ).color(r, g, b, a);
        vb.vertex(matrix, minX, maxY, maxZ).color(r, g, b, a);

        vb.vertex(matrix, minX, minY, minZ).color(r, g, b, a);
        vb.vertex(matrix, minX, minY, maxZ).color(r, g, b, a);
        vb.vertex(matrix, minX, maxY, maxZ).color(r, g, b, a);
        vb.vertex(matrix, minX, maxY, minZ).color(r, g, b, a);

        vb.vertex(matrix, maxX, minY, minZ).color(r, g, b, a);
        vb.vertex(matrix, maxX, minY, maxZ).color(r, g, b, a);
        vb.vertex(matrix, maxX, maxY, maxZ).color(r, g, b, a);
        vb.vertex(matrix, maxX, maxY, minZ).color(r, g, b, a);


        postRender(vb, matrixStack);
    }


    public static void renderHealth(Entity e, RenderWorldEvent event, float currentHealth, float maxHealth, float targetHealthRatio, float delta) {
        float h = (currentHealth / maxHealth);
        int barHeight = (int) (74.0D * h);
        Color healthColor = h < 0.3D ? red : (h < 0.5D ? orange : (h < 0.7D ? yellow : green));

        float x = (float) (e.lastRenderX + (e.getX() - e.lastRenderX) * delta - mc.gameRenderer.getCamera().getPos().x);
        float y = (float) (e.lastRenderY + (e.getY() - e.lastRenderY) * delta - mc.gameRenderer.getCamera().getPos().y);
        float z = (float) (e.lastRenderZ + (e.getZ() - e.lastRenderZ) * delta - mc.gameRenderer.getCamera().getPos().z);

        BufferBuilder bufferBuilder = getBufferBuilder(event.matrixStack, VertexFormat.DrawMode.QUADS);

        preRender();

        MatrixStack matrixStack = event.matrixStack;

        matrixStack.translate(x, y - 0.2D, z);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-mc.getCameraEntity().getYaw()));
        matrixStack.scale(0.03F, 0.03F, 0.03F);

        int barX = 21;
        int barWidth = 4;
        int fullBarHeight = 75;

        Matrix4f matrix = matrixStack.peek().getPositionMatrix();

        bufferBuilder.vertex(matrix, barX, -1, 0).color(black.getRGB());
        bufferBuilder.vertex(matrix, barX + barWidth, -1, 0).color(black.getRGB());
        bufferBuilder.vertex(matrix, barX + barWidth, fullBarHeight, 0).color(black.getRGB());
        bufferBuilder.vertex(matrix, barX, fullBarHeight, 0).color(black.getRGB());

        bufferBuilder.vertex(matrix, barX + 1, (float) barHeight, 0).color(gray.getRGB());
        bufferBuilder.vertex(matrix, barX + barWidth - 1, (float) barHeight, 0).color(gray.getRGB());
        bufferBuilder.vertex(matrix, barX + barWidth - 1, fullBarHeight - 1, 0).color(gray.getRGB());
        bufferBuilder.vertex(matrix, barX + 1, fullBarHeight - 1, 0).color(gray.getRGB());

        bufferBuilder.vertex(matrix, barX + 1, 0, 0).color(healthColor.getRGB());
        bufferBuilder.vertex(matrix, barX + barWidth - 1, 0, 0).color(healthColor.getRGB());
        bufferBuilder.vertex(matrix, barX + barWidth - 1, (float) barHeight, 0).color(healthColor.getRGB());
        bufferBuilder.vertex(matrix, barX + 1, (float) barHeight, 0).color(healthColor.getRGB());
        postRender(bufferBuilder, event.matrixStack);
    }

    static float animationCounter = 0f;

    public static void drawCircle(Entity e, RenderWorldEvent event, float innerRadius, int innerColor, float delta) {
        float innerF3 = (innerColor >> 24 & 255) / 255.0F;
        float innerF = (innerColor >> 16 & 255) / 255.0F;
        float innerF1 = (innerColor >> 8 & 255) / 255.0F;
        float innerF2 = (innerColor & 255) / 255.0F;

        float x = (float) (e.lastRenderX + (e.getX() - e.lastRenderX) * delta - mc.gameRenderer.getCamera().getPos().x);
        float y = (float) (e.lastRenderY + (e.getY() - e.lastRenderY) * delta - mc.gameRenderer.getCamera().getPos().y);
        float z = (float) (e.lastRenderZ + (e.getZ() - e.lastRenderZ) * delta - mc.gameRenderer.getCamera().getPos().z);

        animationCounter += 0.005f;

        float yOffset = (float) (Math.sin(animationCounter) * 0.5 + 0.5) * (e.getHeight() + 0.2f);
        int segments = 360;
        float angleStep = (float) (2 * Math.PI / segments);

        MatrixStack matrixStack = event.matrixStack;
        BufferBuilder bufferBuilder = getBufferBuilder(matrixStack, VertexFormat.DrawMode.QUADS);

        preRender();

        Matrix4f matrix = matrixStack.peek().getPositionMatrix();

        float thickness = 0.2f;
        int layers = 2;
        float layerStep = thickness / layers;

        ArrayList<Vector3f> outerCircleVertices = new ArrayList<>();
        ArrayList<Vector3f> innerCircleVertices = new ArrayList<>();

        for (int layer = 0; layer < layers; layer++) {
            float yf = (y + yOffset) + (layer * layerStep);
            ArrayList<Vector3f> layerVertices = new ArrayList<>();

            for (int i = 0; i <= segments; i++) {
                float angle = angleStep * i;

                float xOffset = innerRadius * (float) Math.cos(angle);
                float zOffset = innerRadius * (float) Math.sin(angle);

                float xx = x + xOffset;
                float yy = yf;
                float zz = z + zOffset;

                layerVertices.add(new Vector3f(xx, yy, zz));
            }

            for (int i = 0; i < layerVertices.size() - 1; i++) {
                Vector3f vertex1 = layerVertices.get(i);
                Vector3f vertex2 = layerVertices.get(i + 1);

                bufferBuilder.vertex(matrix, vertex1.x(), vertex1.y(), vertex1.z()).color(innerF, innerF1, innerF2, innerF3);
                bufferBuilder.vertex(matrix, vertex2.x(), vertex2.y(), vertex2.z()).color(innerF, innerF1, innerF2, innerF3);
            }

            if (layer == 0) {
                outerCircleVertices.addAll(layerVertices);
            } else {
                innerCircleVertices.addAll(layerVertices);
            }
        }

        postRender(bufferBuilder, matrixStack);

        //preRender();

        //bufferBuilder = getBufferBuilder(matrixStack, VertexFormat.DrawMode.QUADS);

        float whiteF = 1.0f;
        float whiteF1 = 1.0f;
        float whiteF2 = 1.0f;
        float whiteF3 = 0.5f;

        for (int i = 0; i < segments; i++) {
            Vector3f outer2 = outerCircleVertices.get(i + 1);
            Vector3f inner2 = innerCircleVertices.get(i + 1);


            //bufferBuilder.vertex(matrix, inner2.x, inner2.y, inner2.z).color(whiteF, whiteF1, whiteF2, whiteF3);
            //bufferBuilder.vertex(matrix, outer2.x, outer2.y, outer2.z).color(whiteF, whiteF1, whiteF2, whiteF3);
            //bufferBuilder.vertex(matrix, -inner2.x, -inner2.y, -inner2.z).color(whiteF, whiteF1, whiteF2, whiteF3);
            //bufferBuilder.vertex(matrix, outer2.x, outer2.y, outer2.z).color(whiteF, whiteF1, whiteF2, whiteF3);
            //bufferBuilder.vertex(matrix, inner2.x, inner2.y, inner2.z).color(whiteF, whiteF1, whiteF2, whiteF3);

            //bufferBuilder.vertex(matrix, inner2.x, inner2.y, outer2.z).color(whiteF, whiteF1, whiteF2, whiteF3);
            //bufferBuilder.vertex(matrix, outer2.x, inner2.y, inner2.z).color(whiteF, whiteF1, whiteF2, whiteF3);
           // bufferBuilder.vertex(matrix, inner2.x, inner2.y, outer2.z).color(whiteF, whiteF1, whiteF2, whiteF3);



        }

        //postRender(bufferBuilder, matrixStack);
    }




    public static void drawTextWithBackground(Matrix4f matrix, String text, float x, float y, int color, RenderTickEvent event) {
        int textWidth = mc.textRenderer.getWidth(text);
        int textHeight = mc.textRenderer.fontHeight;

        float padding = 1.0f;

        float adjustedX = x + padding;
        float adjustedY = y + padding;

        float left = x - padding;
        float top = y - padding;
        float right = x + textWidth + padding * 2;
        float bottom = y + textHeight + padding * 2;

        drawRect(event, left, top, right, bottom, 0x80000000);

        mc.textRenderer.draw(
                text,
                adjustedX,
                adjustedY,
                color,
                true,
                matrix,
                mc.getBufferBuilders().getEntityVertexConsumers(),
                TextRenderer.TextLayerType.NORMAL,
                0,
                255
        );
    }


    public static void drawRect(RenderTickEvent event, float left, float top, float right, float bottom, int color) {
        float f3 = (color >> 24 & 255) / 255.0F;
        float f = (color >> 16 & 255) / 255.0F;
        float f1 = (color >> 8 & 255) / 255.0F;
        float f2 = (color & 255) / 255.0F;
        MatrixStack matrixStack = event.context.getMatrices();

        BufferBuilder bufferBuilder = getBufferBuilder(matrixStack, VertexFormat.DrawMode.QUADS);

        preRender();

        Matrix4f matrix = matrixStack.peek().getPositionMatrix();

        bufferBuilder.vertex(matrix, left, bottom,0.0f).color(f, f1, f2, f3);
        bufferBuilder.vertex(matrix, right, bottom,0.0f).color(f, f1, f2, f3);
        bufferBuilder.vertex(matrix, right, top,0.0f).color(f, f1, f2, f3);
        bufferBuilder.vertex(matrix, left, top,0.0f).color(f, f1, f2, f3);

        postRender(bufferBuilder, matrixStack);
    }



    public static void drawRect(DrawContext event, float left, float top, float right, float bottom, int color) {
        float f3 = (color >> 24 & 255) / 255.0F;
        float f = (color >> 16 & 255) / 255.0F;
        float f1 = (color >> 8 & 255) / 255.0F;
        float f2 = (color & 255) / 255.0F;
        MatrixStack matrixStack = event.getMatrices();

        BufferBuilder bufferBuilder = getBufferBuilder(matrixStack, VertexFormat.DrawMode.QUADS);

        preRender();

        Matrix4f matrix = matrixStack.peek().getPositionMatrix();

        bufferBuilder.vertex(matrix, left, bottom,0.0f).color(f, f1, f2, f3);
        bufferBuilder.vertex(matrix, right, bottom,0.0f).color(f, f1, f2, f3);
        bufferBuilder.vertex(matrix, right, top,0.0f).color(f, f1, f2, f3);
        bufferBuilder.vertex(matrix, left, top,0.0f).color(f, f1, f2, f3);

        postRender(bufferBuilder, matrixStack);
    }


    public static void drawCircle(DrawContext event, float centerX, float centerY, float radius, int color) {
        float alpha = (color >> 24 & 255) / 255.0F;
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;

        MatrixStack matrixStack = event.getMatrices();
        BufferBuilder bufferBuilder = getBufferBuilder(matrixStack, VertexFormat.DrawMode.TRIANGLE_FAN);
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();

        preRender();

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glEnable(GL11.GL_BLEND);

        //bufferBuilder.vertex(matrix, centerX, centerY, 0.0f).color(red, green, blue, alpha);

        final float step = 0.05f;
        final int segments = (int) (2 * Math.PI / step);

        for (int i = 0; i < segments; i++) {
            float angle = i * step;
            float x = centerX + MathHelper.cos(angle) * radius;
            float y = centerY + MathHelper.sin(angle) * radius;

            bufferBuilder.vertex(matrix, x, y, 0.0f).color(red, green, blue, alpha);
            bufferBuilder.vertex(matrix, x, y, 0.0f).color(red, green, blue, alpha);
        }

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);

        postRender(bufferBuilder, matrixStack);
    }




    public static int interpolateColor(int startColor, int endColor, float ratio) {
        int sr = (startColor >> 16) & 0xFF, sg = (startColor >> 8) & 0xFF, sb = startColor & 0xFF;
        int er = (endColor >> 16) & 0xFF, eg = (endColor >> 8) & 0xFF, eb = endColor & 0xFF;
        int r = (int) (sr + ratio * (er - sr)), g = (int) (sg + ratio * (eg - sg)), b = (int) (sb + ratio * (eb - sb));
        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }

    public static void preRender() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    public static BufferBuilder getBufferBuilder(MatrixStack matrixStack, VertexFormat.DrawMode drawMode) {
        matrixStack.push();

        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        return tessellator.begin(drawMode, VertexFormats.POSITION_COLOR);
    }

    public static void postRender(BufferBuilder bufferBuilder, MatrixStack matrixStack) {
        matrixStack.pop();

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

        RenderSystem.setShaderColor(1,1,1,1);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }
}
