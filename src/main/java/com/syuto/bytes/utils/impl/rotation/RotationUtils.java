package com.syuto.bytes.utils.impl.rotation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;

import static com.syuto.bytes.Byte.mc;

public class RotationUtils {


    public static float[] getRotationsToEntity(Entity entity) {
        if (entity == null) {
            return null;
        }

        // Player position
        double playerX = mc.player.getX();
        double playerY = mc.player.getEyeY(); // Eye height for accurate targeting
        double playerZ = mc.player.getZ();

        // Entity position
        double targetX = entity.getX();
        double targetZ = entity.getZ();
        double targetY;

        // Determine target Y based on entity type
        if (entity instanceof LivingEntity livingEntity) {
            targetY = livingEntity.getEyeY(); // Use the entity's eye height
        } else {
            Box boundingBox = entity.getBoundingBox();
            targetY = (boundingBox.minY + boundingBox.maxY) / 2.0; // Center of the bounding box
        }

        // Calculate deltas
        double deltaX = targetX - playerX;
        double deltaZ = targetZ - playerZ;
        double deltaY = targetY - playerY;

        // Calculate yaw
        float yaw = (float) Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0F;
        yaw = MathHelper.wrapDegrees(yaw); // Wrap to -180 to 180

        // Calculate pitch
        double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        float pitch = (float) -Math.toDegrees(Math.atan2(deltaY, horizontalDistance));
        pitch = MathHelper.clamp(pitch, -90.0F, 90.0F); // Clamp to -90 to 90

        return new float[]{yaw, pitch};
    }
}
