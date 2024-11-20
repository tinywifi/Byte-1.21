package com.syuto.bytes.eventbus.impl;

import com.syuto.bytes.eventbus.Event;

public class RenderWorldEvent implements Event {

    public float partialTicks;

    public RenderWorldEvent(float partialTicks) { this.partialTicks = partialTicks; }

}
