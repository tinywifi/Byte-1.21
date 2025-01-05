package com.syuto.bytes.utils.impl.player;

import static com.syuto.bytes.Byte.mc;

public class MovementUtil {
    public static double[] setSpeed(double speed) {
        double dir = direction();

        return new double[] {-Math.sin(dir) * speed, Math.cos(dir) * speed};
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
}
