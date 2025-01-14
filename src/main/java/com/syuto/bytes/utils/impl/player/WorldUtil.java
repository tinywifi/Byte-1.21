package com.syuto.bytes.utils.impl.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

import static com.syuto.bytes.Byte.mc;

public class WorldUtil {


    public static BlockPos findBlocks(BlockPos pos, int range) {
        Optional<BlockPos> block = BlockPos.findClosest(
                pos.down(), range, range,
                cock -> !mc.world.getBlockState(cock).isAir()
        );
        return block.orElse(null);
    }


    public static Direction getClosest(BlockPos pos) {
        double closestDistance = Double.MAX_VALUE;
        Direction closestDirection = null;
        for (Direction dir : Direction.values()) {
            Vec3d faceCenter = pos.offset(dir).toCenterPos();
            double distance = mc.player.getPos().squaredDistanceTo(faceCenter);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestDirection = dir;
            }
        }

        return closestDirection;
    }




    public static boolean canBePlacedOn(BlockPos blockPos) {
        if (blockPos == null) return false;
        Box playerBox = mc.player.getBoundingBox();

        double minX = blockPos.getX();
        double minY = blockPos.getY();
        double minZ = blockPos.getZ();
        Box blockBox = new Box(minX, minY, minZ, minX + 1, minY + 1, minZ + 1);
        if (playerBox.intersects(blockBox)) {
                return false;
        }

        BlockState blockState = mc.world.getBlockState(blockPos);
        return blockState.isSolid() || blockState.isReplaceable();
    }

}
