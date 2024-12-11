package com.syuto.bytes.module.impl.render;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.RenderTickEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.utils.impl.render.RenderUtils;

import java.awt.*;

import static com.syuto.bytes.Byte.mc;

public class RenderTest extends Module {
    public RenderTest() {
        super("RenderTest", "rendertest", Category.RENDER);
    }

    private final Color red = Color.RED;

    @EventHandler
    void onRenderTick(RenderTickEvent event) {
        int width = mc.getWindow().getScaledWidth();
        int height = mc.getWindow().getScaledHeight();

        int boxWidth = 200;
        int boxHeight = 200;


        float left = (width / 2.0f) - (boxWidth / 2.0f);
        float top = (height / 2.0f) - (boxHeight / 2.0f);
        float right = left + boxWidth;
        float bottom = top + boxHeight;

        float centerX = width / 2.0f;
        float centerY = height / 2.0f;

        //RenderUtils.drawCircle(event, centerX, centerY, 50, red.getRGB());
        //RenderUtils.drawRect(event, left, top, right, bottom, red.getRGB());
    }
}
