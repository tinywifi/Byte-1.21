package com.syuto.bytes.eventbus.impl;

import com.syuto.bytes.eventbus.Event;

public class ChatEvent implements Event {
    private String message;
    private boolean canceled;

    public ChatEvent(String message) {
        this.message = message;
        this.canceled = false;
    }

    public String getMessage() {
        return message;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public boolean isCanceled() {
        return canceled;
    }
}
