package com.syuto.bytes.eventbus.impl;

import com.syuto.bytes.eventbus.Event;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RotationEvent implements Event {
    public float yaw, pitch;

    public RotationEvent(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

}
