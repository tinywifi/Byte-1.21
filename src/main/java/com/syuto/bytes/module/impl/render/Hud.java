package com.syuto.bytes.module.impl.render;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.RenderTickEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import net.minecraft.text.Text;
import org.joml.Matrix4f;

import java.awt.*;

import static com.syuto.bytes.Byte.mc;

public class Hud extends Module {
    public Hud() {
        super("Hud", "hud bro", Category.RENDER);
    }

    Color darkblue = Color.blue;
    Color cyan = Color.cyan;


    @EventHandler
    public void onRenderTick(RenderTickEvent event) {
        mc.textRenderer.drawWithOutline(
                Text.literal("</byte>").asOrderedText(),
                5,
                10,
                cyan.getRGB(),
                darkblue.getRGB(),
                new Matrix4f(),
                mc.getBufferBuilders().getEntityVertexConsumers(),
                255
        );
    }
}
