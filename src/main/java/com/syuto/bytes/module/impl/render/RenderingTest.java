package com.syuto.bytes.module.impl.render;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.RenderTickEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.utils.impl.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

import java.awt.*;

import static com.syuto.bytes.Byte.mc;

public class RenderingTest extends Module {
    public RenderingTest() {
        super("RenderingTest", "r", Category.RENDER);
    }


    @EventHandler
    void onRenderTick(RenderTickEvent event) {
        DrawContext context = event.context;

        int width = mc.getWindow().getScaledWidth();
        int height = mc.getWindow().getScaledHeight();

        MatrixStack matrixStack = context.getMatrices();

        float left = width/2f - 25;
        float right = width/2f + 25;
        float top = height/2f - 25;
        float bottom = height/2f + 25;


        drawRect(
                matrixStack,
                left,
                right,
                top,
                bottom,
                Color.GREEN.getRGB()
        );
    }


    public void drawRect(MatrixStack matrixStack, float left, float right, float top, float bottom, int color) {
        BufferBuilder bufferBuilder = RenderUtils.getBufferBuilder(matrixStack, VertexFormat.DrawMode.QUADS);
        RenderUtils.preRender();
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();

        bufferBuilder.vertex(matrix, left, bottom, 0.0f).color(color);
        bufferBuilder.vertex(matrix, right, bottom, 0.0f).color(color);
        bufferBuilder.vertex(matrix, right, top, 0.0f).color(color);
        bufferBuilder.vertex(matrix, left, top, 0.0f).color(color);

        RenderUtils.postRender(bufferBuilder, matrixStack);
    }
}
