package com.syuto.bytes.utils.impl.rotation;

import com.syuto.bytes.utils.impl.client.ChatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static com.syuto.bytes.Byte.mc;

public class RotationUtils {

    public static float[] getFixedRotation(final float[] rotations, final float[] lastRotations) {

        float sensitivity = (float) (mc.options.getMouseSensitivity().getValue() * 0.8f);
        float gcd = sensitivity * sensitivity * sensitivity * 1.2f;

        float deltaYaw = rotations[0] - lastRotations[0];
        float deltaPitch = rotations[1] - lastRotations[1];

        float fixedYaw = lastRotations[0] + (deltaYaw - (deltaYaw % gcd));
        float fixedPitch = lastRotations[1] + (deltaPitch - (deltaPitch % gcd));

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

    public static float[] getRotationsToBlock(Vec3d eye, BlockPos blockPos, Direction face) {
        Vec3d target = new Vec3d(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
        target = target.add(new Vec3d(face.getOffsetX() * 0.5, face.getOffsetY() * 0.5, face.getOffsetZ() * 0.5));
        double diffX = target.x - eye.x;
        double diffY = target.y - eye.y;
        double diffZ = target.z - eye.z;

        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float pitch = (float) -Math.atan2(dist, diffY);
        float yaw = (float) Math.atan2(diffZ, diffX);
        pitch = (float) ((pitch * 180F) / Math.PI + 90) * -1;
        yaw = (float) ((yaw * 180) / Math.PI) - 90;

        return new float[]{yaw, clampPitch(pitch)};
    }


    public static float[] getBlockRotations(BlockPos blockPos, Direction facing) {
        double d = blockPos.getX() + 0.5 - mc.player.getX() + facing.getOffsetX() * 0.5;
        double d2 = blockPos.getZ() + 0.5 - mc.player.getZ() + facing.getOffsetZ() * 0.5;
        double d3 = mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()) - blockPos.getY() - facing.getOffsetY() * 0.5;
        double d4 = Math.sqrt(d * d + d2 * d2);
        float yaw = (float) (Math.atan2(d2, d) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float) (Math.atan2(d3, d4) * 180.0 / Math.PI);

        return new float[]{MathHelper.wrapDegrees(yaw), clampPitch(pitch)};
    }

    private static float clampPitch(float pitch) {
        return MathHelper.clamp(pitch, -90.0F, 90.0F);
    }
}
