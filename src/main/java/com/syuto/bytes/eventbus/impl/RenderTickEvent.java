package com.syuto.bytes.eventbus.impl;

import com.syuto.bytes.eventbus.Event;

public class RenderTickEvent implements Event {

    public float partialTicks;

    public RenderTickEvent(float partialTicks) { this.partialTicks = partialTicks; }

}
