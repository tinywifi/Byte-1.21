package com.syuto.bytes.utils.impl.player;

import net.minecraft.client.Mouse;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.MaceItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.Optional;

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
        Vec3d startPos = mc.player.getCameraPosVec(tickDelta);
        Vec3d direction = mc.player.getRotationVector(pitch, yaw);
        Vec3d endPos = startPos.add(direction.multiply(maxDistance));

        HitResult blockHit = mc.world.raycast(new RaycastContext(
                startPos, endPos,
                RaycastContext.ShapeType.OUTLINE,
                includeFluids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE,
                mc.player
        ));

        EntityHitResult entityHit = raycastEntities(mc.world, mc.player, startPos, endPos, maxDistance);

        if (entityHit != null && (blockHit == null || entityHit.getPos().squaredDistanceTo(startPos) < blockHit.getPos().squaredDistanceTo(startPos))) {
            return entityHit;
        }

        return null;
    }

    // Entity raycasting method (manual)
    private static EntityHitResult raycastEntities(World world, PlayerEntity player, Vec3d startPos, Vec3d endPos, double maxDistance) {
        EntityHitResult closestEntityHit = null;
        double closestDistanceSq = maxDistance * maxDistance;

        Box searchBox = new Box(startPos, endPos).expand(1.0);

        for (Entity entity : world.getOtherEntities(player, searchBox)) {
            if (!entity.isAlive() || entity.isSpectator()) continue;

            Box entityBox = entity.getBoundingBox().expand(0.1);
            Optional<Vec3d> intersection = entityBox.raycast(startPos, endPos);
            if (intersection.isPresent()) {
                double distanceSq = startPos.squaredDistanceTo(intersection.get());
                if (distanceSq < closestDistanceSq) {
                    closestDistanceSq = distanceSq;
                    closestEntityHit = new EntityHitResult(entity, intersection.get());
                }
            }
        }

        return closestEntityHit;
    }



    public static boolean isHoldingWeapon() {
        if (mc.player.getMainHandStack() != null) {
            Item item = mc.player.getMainHandStack().getItem();
            return item instanceof SwordItem || item instanceof AxeItem || item instanceof MaceItem;
        }
        return false;
    }
}
