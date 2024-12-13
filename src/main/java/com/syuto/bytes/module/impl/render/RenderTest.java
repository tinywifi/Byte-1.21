package com.syuto.bytes.module.impl.render;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.RenderTickEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.utils.impl.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

import java.awt.*;

import static com.syuto.bytes.Byte.mc;

public class RenderTest extends Module {
    public RenderTest() {
        super("RenderTest", "rendertest", Category.RENDER);
    }

    private final Color red = Color.RED;

    @EventHandler
    void onRenderTick(RenderTickEvent event) {
        MatrixStack matrices = event.context.getMatrices();
        Matrix4f matrix = matrices.peek().getPositionMatrix();


        int width = mc.getWindow().getScaledWidth();
        int height = mc.getWindow().getScaledHeight();


        float x = (width / 2.0f);
        float y = (height / 2.0f);

        RenderUtils.drawCircle(event, x, y,50,red.getRGB());
        //RenderUtils.drawTextWithBackground(matrix, "Hello", x, y, red.getRGB(), event);


    }
}
