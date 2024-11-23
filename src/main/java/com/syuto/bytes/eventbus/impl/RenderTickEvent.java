package com.syuto.bytes.eventbus.impl;

import com.syuto.bytes.eventbus.Event;
import net.minecraft.client.gui.DrawContext;

public class RenderTickEvent implements Event {

    public float partialTicks;
    public DrawContext context;

    public RenderTickEvent(float partialTicks, DrawContext context) {
        this.partialTicks = partialTicks;
        this.context = context;
    }

}
