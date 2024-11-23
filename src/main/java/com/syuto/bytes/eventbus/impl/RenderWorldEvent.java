package com.syuto.bytes.eventbus.impl;

import com.syuto.bytes.eventbus.Event;
import net.minecraft.client.util.math.MatrixStack;

public class RenderWorldEvent implements Event {

    public float partialTicks;
    public MatrixStack matrixStack;

    public RenderWorldEvent(float partialTicks, MatrixStack matrixStack) {
        this.partialTicks = partialTicks;
        this.matrixStack = matrixStack;
    }

}
