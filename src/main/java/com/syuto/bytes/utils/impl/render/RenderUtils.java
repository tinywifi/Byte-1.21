package com.syuto.bytes.utils.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.syuto.bytes.eventbus.impl.RenderWorldEvent;
import com.syuto.bytes.utils.impl.client.ChatUtils;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.datafixer.fix.ChunkPalettedStorageFix;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.opengl.GL11;

import java.awt.*;

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

    public static Vec3d calculateOffset(double x, double y, double z, float yaw, float pitch) {
        double yawRad = Math.toRadians(yaw);
        double pitchRad = Math.toRadians(pitch);
        double xOffset = -Math.sin(yawRad) * Math.cos(pitchRad);
        double yOffset = -Math.sin(pitchRad);
        double zOffset = Math.cos(yawRad) * Math.cos(pitchRad);
        return new Vec3d(x + xOffset, y + yOffset, z + zOffset);
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
