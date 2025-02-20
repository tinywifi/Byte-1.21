package com.syuto.bytes.utils.impl.player;

import static com.syuto.bytes.Byte.mc;

public class MovementUtil {
    public static void setSpeed(double speed) {
        double dir = direction();
        mc.player.setVelocity(-Math.sin(dir) * speed, mc.player.getVelocity().y, Math.cos(dir) * speed);
    }

    public static float direction() {
        float rotationYaw = mc.player.getYaw();
        if (mc.player.forwardSpeed < 0) {
            rotationYaw += 180;
        }

        float forward = 1;

        if (mc.player.forwardSpeed  < 0) {
            forward = -0.5F;
        } else if (mc.player.forwardSpeed  > 0) {
            forward = 0.5F;
        }

        if (mc.player.sidewaysSpeed > 0) {
            rotationYaw -= 90 * forward;
        }
        if (mc.player.sidewaysSpeed < 0) {
            rotationYaw += 90 * forward;
        }
        return (float) Math.toRadians(rotationYaw);
    }

    public static float[] move(float yaw) {
        float radians = (float) Math.toRadians(yaw);
        float forward = (float) -Math.cos(radians);
        float sideways = (float) Math.sin(radians);

        return new float[]{forward, sideways};
    }


    public static float directionAtan() {
        return (float) Math.toDegrees(Math.atan2(-mc.player.getVelocity().x, mc.player.getVelocity().z));
    }


    public static boolean isMoving() {
        return mc.player.forwardSpeed > 0 || mc.player.forwardSpeed < 0 || mc.player.sidewaysSpeed > 0 || mc.player.sidewaysSpeed < 0;
    }
}
