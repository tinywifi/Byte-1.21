package com.syuto.bytes.utils.impl.rotation;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public class RotationUtils {

    public static float[] getRotations(Entity entity) {
        if (entity == null) {
            return null;
        }

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return null;
        }

        double deltaX = entity.getX() - player.getX();
        double deltaZ = entity.getZ() - player.getZ();

        double deltaY;
        if (entity instanceof LivingEntity livingEntity) {
            deltaY = livingEntity.getEyeY() - player.getEyeY();
        } else {
            deltaY = (entity.getBoundingBox().minY + entity.getBoundingBox().maxY) / 2.0 - player.getEyeY();
        }

        float yaw = (float) (MathHelper.atan2(deltaZ, deltaX) * (180.0 / Math.PI)) - 90.0F;
        yaw = MathHelper.wrapDegrees(yaw - player.getYaw()) + player.getYaw();

        double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        float pitch = (float) (-(MathHelper.atan2(deltaY, horizontalDistance) * (180.0 / Math.PI)));
        pitch = MathHelper.wrapDegrees(pitch - player.getPitch()) + player.getPitch();

        return new float[]{yaw, clampPitch(pitch)};
    }

    private static float clampPitch(float pitch) {
        return MathHelper.clamp(pitch, -90.0F, 90.0F);
    }


}
