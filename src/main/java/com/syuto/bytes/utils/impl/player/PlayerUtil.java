package com.syuto.bytes.utils.impl.player;

import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import static com.syuto.bytes.Byte.mc;

public class PlayerUtil {


    public static double getBiblicallyAccurateDistanceToEntity(Entity target) {
        return mc.player.getEyePos().distanceTo(getClosestPoint(target));
    }

    private static Vec3d getClosestPoint(Entity target) {
        Box hb = target.getBoundingBox();
        Vec3d start = getBoxMinPoint(hb);
        Vec3d s = getBoxSize(hb);
        Vec3d eP = mc.player.getEyePos();

        Vec3d closest = null;
        double closestDist = Double.MAX_VALUE;
        double stepSize = 0.05d;
        for (double dX = 0; dX <= s.x; dX += stepSize) {
            for (double dY = 0; dY <= s.y; dY += stepSize) {
                for (double dZ = 0; dZ <= s.z; dZ += stepSize) {
                    Vec3d point = start.add(dX, dY, dZ);
                    if (!hb.contains(point)) continue;
                    double d = point.squaredDistanceTo(eP);
                    if (d < closestDist) {
                        closestDist = d;
                        closest = point;
                    }
                }
            }
        }
        return closest;
    }


    private static Vec3d getBoxSize(Box box) {
        double hbX = box.maxX - box.minX;
        double hbY = box.maxY - box.minY;
        double hbZ = box.maxZ - box.minZ;
        return new Vec3d(hbX, hbY, hbZ);
    }

    private static Vec3d getBoxMinPoint(Box box) {
        return new Vec3d(box.minX, box.minY, box.minZ);
    }

    public static HitResult raycast(float yaw, float pitch, double maxDistance, float tickDelta, boolean includeFluids) {
        Vec3d vec3d = mc.player.getCameraPosVec(tickDelta);
        Vec3d vec3d2 = mc.player.getRotationVector(pitch, yaw);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * maxDistance, vec3d2.y * maxDistance, vec3d2.z * maxDistance);
        return mc.world.raycast(new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.OUTLINE, includeFluids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE, mc.player));
    }
}
