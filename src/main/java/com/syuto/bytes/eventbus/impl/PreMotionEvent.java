package com.syuto.bytes.eventbus.impl;

import com.syuto.bytes.eventbus.Event;

public class PreMotionEvent implements Event {
    public static double posX;
    public static double posY;
    public static double posZ;
    public static float yaw;
    public static float pitch;
    public static float lastYaw;
    public static float lastPitch;
    public static boolean onGround;
    public static boolean isSprinting;
    public static boolean isSneaking;
    public static boolean horizontalCollision;
    public static boolean pitchChanged = false;

    public PreMotionEvent(double posX, double posY, double posZ, float yaw, float pitch, float lastYaw, float lastPitch, boolean onGround, boolean isSprinting, boolean isSneaking, boolean horizontalCollision) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.lastYaw = lastYaw;
        this.lastPitch = lastPitch;
        this.onGround = onGround;
        this.isSprinting = isSprinting;
        this.isSneaking = isSneaking;
        this.horizontalCollision = horizontalCollision;
    }
}