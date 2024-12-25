package com.syuto.bytes.eventbus.impl;

import com.syuto.bytes.eventbus.Event;
import lombok.Getter;
import lombok.Setter;

public class ChatEvent implements Event {
    @Getter
    @Setter
    private String message;
    @Getter
    @Setter
    private boolean canceled;

    public ChatEvent(String message) {
        this.message = message;
        this.canceled = false;
    }
}
