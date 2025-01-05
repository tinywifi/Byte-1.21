package com.syuto.bytes.utils.impl.rotation;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static com.syuto.bytes.Byte.mc;

public class RotationUtils {

    public static float[] getFixedRotation(final float[] rotations, final float[] lastRotations) {

        final float yaw = rotations[0];
        final float pitch = rotations[1];

        final float lastYaw = lastRotations[0];
        final float lastPitch = lastRotations[1];
        final float f = (float) (mc.options.getMouseSensitivity().getValue() * 0.6F + 0.2F);
        final float gcd = f * f * f * 1.2F;

        final float deltaYaw = yaw - lastYaw;
        final float deltaPitch = pitch - lastPitch;

        final float fixedDeltaYaw = deltaYaw - (deltaYaw % gcd);
        final float fixedDeltaPitch = deltaPitch - (deltaPitch % gcd);

        final float fixedYaw = lastYaw + fixedDeltaYaw;
        final float fixedPitch = lastPitch + fixedDeltaPitch;

        return new float[]{fixedYaw, fixedPitch};
    }


    public static float[] getRotations(Vec3d eye, Entity entity) {
        Vec3d to = entity.getEyePos();
        double diffX = to.x - eye.x;
        double diffY = to.y - eye.y;
        double diffZ = to.z - eye.z;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float pitch = (float) -Math.atan2(dist, diffY);
        float yaw = (float) Math.atan2(diffZ, diffX);

        pitch = (float) ((pitch * 180F) / Math.PI + 90) * -1;
        yaw = (float) ((yaw * 180) / Math.PI) - 90;


        return new float[]{yaw, clampPitch(pitch)};
    }


    private static float clampPitch(float pitch) {
        return MathHelper.clamp(pitch, -90.0F, 90.0F);
    }


}
