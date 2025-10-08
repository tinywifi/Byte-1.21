package com.syuto.bytes.module.impl.player;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PreUpdateEvent;
import com.syuto.bytes.eventbus.impl.RenderWorldEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.utils.impl.client.ChatUtils;
import com.syuto.bytes.utils.impl.player.PathFinder;
import com.syuto.bytes.utils.impl.render.RenderUtils;
import com.syuto.bytes.utils.impl.rotation.RotationUtils;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PathFind extends Module {
    private Queue<BlockPos> path = new LinkedList<>();
    private boolean isPathing = false;

    public PathFind() {
        super("PathFinder", "Path finds to coords", Category.PLAYER);
    }

    @EventHandler
    void onRenderWorld(RenderWorldEvent event) {
        if (path == null || path.isEmpty()) return;

        for (BlockPos pos : path) {
            //RenderUtils.renderBlock(pos, event, event.partialTicks);
        }
    }

    public void startPathfinding(double x, double y, double z) {
        BlockPos start = mc.player.getBlockPos();
        BlockPos target = new BlockPos((int) x, (int) y, (int) z);
        List<BlockPos> result = PathFinder.pathFind(start, target);
        if (!result.isEmpty()) {
            path.clear();
            path.addAll(result);
            isPathing = true;
        } else {
            ChatUtils.print("No path found.");
        }
    }

    @EventHandler
    void onPreUpdate(PreUpdateEvent event) {
        if (!isPathing || path.isEmpty()) {
            resetKeys();
            return;
        }

        BlockPos next = path.peek();
        Vec3d playerPos = mc.player.getPos();
        Vec3d targetPos = Vec3d.ofCenter(next);
        double distance = playerPos.squaredDistanceTo(targetPos);

        Vec3d d = targetPos.subtract(playerPos);

        if (distance < 0.5) {
            path.poll();
            return;
        }

        mc.player.setVelocity(d.x * 0.6, mc.player.getVelocity().y, d.z * 0.6);


    }

    private void faceToward(Vec3d target) {
        float[] rot = RotationUtils.getBlockRotations(BlockPos.ofFloored(target.x, target.y, target.z), Direction.UP);
        mc.player.setYaw((float) rot[0]);
    }

    private void pressKey(KeyBinding key, boolean pressed) {
        key.setPressed(pressed);
    }

    private void resetKeys() {
        pressKey(mc.options.forwardKey, false);
        pressKey(mc.options.backKey, false);
        pressKey(mc.options.leftKey, false);
        pressKey(mc.options.rightKey, false);
        pressKey(mc.options.jumpKey, false);
    }

    @Override
    public void onDisable() {
        resetKeys(); // Ensure no keys stick when disabling
    }
}
