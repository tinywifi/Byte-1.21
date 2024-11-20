package com.syuto.bytes.eventbus.impl;

import com.syuto.bytes.eventbus.Event;

import static com.syuto.bytes.Byte.mc;

public class PreMotionEvent implements Event {
    public double posX;
    public double posY;
    public double posZ;
    public float yaw;
    public float pitch;
    public boolean onGround;
    public boolean isSprinting;
    public boolean isSneaking;
    public boolean horizontalCollision;

    public PreMotionEvent(double posX, double posY, double posZ, float yaw, float pitch, boolean onGround, boolean isSprinting, boolean isSneaking, boolean horizontalCollision) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.isSprinting = isSprinting;
        this.isSneaking = isSneaking;
        this.horizontalCollision = horizontalCollision;
    }
}