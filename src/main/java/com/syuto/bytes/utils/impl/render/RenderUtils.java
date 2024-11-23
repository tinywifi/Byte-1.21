package com.syuto.bytes.utils.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.syuto.bytes.eventbus.impl.RenderWorldEvent;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.syuto.bytes.Byte.mc;

public class RenderUtils {
    private static final Color black = Color.black;
    private static final Color gray = Color.darkGray;
    private static final Color green = Color.green;
    private static final Color yellow = Color.yellow;
    private static final Color red = Color.red;
    private static final Color orange = Color.orange;
    private static final Color white = Color.white;

    private static final Map<Entity, AnimationState> animationStatesMap = new HashMap<>();

    public static void renderHealth(Entity e, RenderWorldEvent event, float currentHealth, float maxHealth, float targetHealthRatio, float delta) {
        AnimationState animationState = animationStatesMap.getOrDefault(e, new AnimationState(targetHealthRatio, 0));
        animationStatesMap.put(e, animationState);
        if (animationState.targetHealthRatio != targetHealthRatio) {
            animationState.startHealthRatio = animationState.displayedHealthRatio;
            animationState.targetHealthRatio = targetHealthRatio;
            animationState.progress = 0;
        }

        animationState.progress = Math.min(animationState.progress + 0.005, 1);

        double easedProgress = easeOutElastic(animationState.progress);
        animationState.displayedHealthRatio = animationState.startHealthRatio
                + (animationState.targetHealthRatio - animationState.startHealthRatio) * easedProgress;

        int barHeight = (int) (74.0D * animationState.displayedHealthRatio);
        Color healthColor = animationState.displayedHealthRatio < 0.3D ? red :
                (animationState.displayedHealthRatio < 0.5D ? orange :
                        (animationState.displayedHealthRatio < 0.7D ? yellow : green));

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

    private static double easeOutElastic(double x) {
        return Math.sin((x * Math.PI) / 2);
    }

    private static class AnimationState {
        double startHealthRatio;
        double targetHealthRatio;
        double displayedHealthRatio;
        double progress;

        AnimationState(float initialRatio, float progress) {
            this.startHealthRatio = initialRatio;
            this.targetHealthRatio = initialRatio;
            this.displayedHealthRatio = initialRatio;
            this.progress = progress;
        }
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
