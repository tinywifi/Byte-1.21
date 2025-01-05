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


    public static boolean isMoving() {
        return mc.player.getMovementSpeed() > 0;
    }
}
