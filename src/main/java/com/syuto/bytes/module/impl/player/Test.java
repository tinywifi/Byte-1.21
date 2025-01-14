package com.syuto.bytes.module.impl.player;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PreMotionEvent;
import com.syuto.bytes.eventbus.impl.PreUpdateEvent;
import com.syuto.bytes.eventbus.impl.RenderWorldEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.setting.impl.NumberSetting;
import com.syuto.bytes.utils.impl.client.ChatUtils;
import com.syuto.bytes.utils.impl.player.WorldUtil;
import com.syuto.bytes.utils.impl.render.RenderUtils;
import com.syuto.bytes.utils.impl.rotation.RotationUtils;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;


public class Test extends Module {

    public NumberSetting range = new NumberSetting("range", this, 3, 3, 10, 1);

    public Test() {
        super("Test", "Testing module", Category.PLAYER);
    }


    private BlockPos block, placePos;
    private Direction facing;
    private float[] rots;


    @EventHandler
    void onRenderWorld(RenderWorldEvent event) {
        if (block != null) {
            RenderUtils.renderBlock(block, event, delta);
        }
    }

    @EventHandler
    void onPreUpdate(PreUpdateEvent event) {
        for(int i = 0; i < 2; i++) {
            this.block = WorldUtil.findBlocks(
                    mc.player.getBlockPos(),
                    range.getValue().intValue()

            );

            if (this.block != null) {
                this.facing = WorldUtil.getClosest(this.block);
                if (this.facing != null && WorldUtil.canBePlacedOn(this.block)) {
                    this.place(block, facing);
                }
            }
        }
    }


    @EventHandler
    void onPreMotion(PreMotionEvent event) {

        if (this.block != null) {
            ChatUtils.print(this.facing.asString());
            this.rots = RotationUtils.getBlockRotations(this.block.down(3), this.facing);
            block = null;
        }


        if (this.rots != null) {
            event.yaw = rots[0];
            event.pitch = rots[1];

            float f = MathHelper.wrapDegrees(event.yaw - mc.player.bodyYaw);
            mc.player.bodyYaw += f * 0.3F;
            mc.player.headYaw = event.yaw;
            if (Math.abs(f) > 50.0f) {
                mc.player.bodyYaw += f - (float) MathHelper.sign(f) * 50.0f;
            }
        }
    }

    private void place(BlockPos pos, Direction direction) {
        ChatUtils.print(direction.asString());
        BlockHitResult result = new BlockHitResult(
                pos.toCenterPos(),
                direction,
                pos,
                false
        );

        mc.interactionManager.interactBlock(mc.player, mc.player.getActiveHand(), result);
        mc.player.swingHand(mc.player.getActiveHand());

    }

}