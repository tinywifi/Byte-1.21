package com.syuto.bytes.eventbus.impl;

import com.syuto.bytes.eventbus.Event;
import lombok.Setter;

public class SlowDownEvent implements Event {
    private final Mode mode;
    @Setter
    private boolean canceled;

    public SlowDownEvent(final Mode mode) {
        this.mode = mode;
        this.canceled = false;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public Mode getMode() {
        return mode;
    }

    public enum Mode {
        Item, Sprint
    }
}