package com.syuto.bytes.module.impl.player;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PreMotionEvent;
import com.syuto.bytes.eventbus.impl.PreUpdateEvent;
import com.syuto.bytes.eventbus.impl.RenderWorldEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.setting.impl.NumberSetting;
import com.syuto.bytes.utils.impl.client.ChatUtils;
import com.syuto.bytes.utils.impl.player.MovementUtil;
import com.syuto.bytes.utils.impl.player.PlayerUtil;
import com.syuto.bytes.utils.impl.player.WorldUtil;
import com.syuto.bytes.utils.impl.render.RenderUtils;
import com.syuto.bytes.utils.impl.rotation.RotationUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.List;
import java.util.stream.IntStream;

import static com.syuto.bytes.Byte.mc;


public class Test extends Module {

    public NumberSetting range = new NumberSetting("range", this, 3, 3, 50, 1);

    public Test() {
        super("Test", "Testing module", Category.PLAYER);
    }


    private BlockPos block, placePos;
    private Direction facing;
    private float[] rots;
    private int ticks;
    List<BlockPos> pos;

    @EventHandler
    void onRenderWorld(RenderWorldEvent event) {
        if (pos != null && !pos.isEmpty()) {
            for (BlockPos po : pos) {
                Block e = mc.world.getBlockState(po).getBlock();
                if (e == Blocks.DIAMOND_ORE || e == Blocks.DEEPSLATE_DIAMOND_ORE) {
                    RenderUtils.renderBlock(po, event, Color.CYAN);
                } else if (e == Blocks.IRON_ORE || e == Blocks.DEEPSLATE_IRON_ORE) {
                    RenderUtils.renderBlock(po, event, Color.WHITE);
                } else if (e == Blocks.COAL_ORE || e == Blocks.DEEPSLATE_COAL_ORE) {
                    RenderUtils.renderBlock(po, event, Color.BLACK);
                } else if (e == Blocks.LAPIS_ORE || e == Blocks.DEEPSLATE_LAPIS_ORE) {
                    RenderUtils.renderBlock(po, event, Color.BLUE);
                } else if (e == Blocks.ANCIENT_DEBRIS) {
                    RenderUtils.renderBlock(po, event, Color.MAGENTA);
                } else if (e == Blocks.EMERALD_ORE || e == Blocks.DEEPSLATE_EMERALD_ORE){
                    RenderUtils.renderBlock(po, event, Color.GREEN);
                } else if (e == Blocks.GOLD_ORE || e == Blocks.DEEPSLATE_GOLD_ORE || e == Blocks.GOLD_BLOCK) {
                    RenderUtils.renderBlock(po, event, Color.YELLOW);
                } else {
                    RenderUtils.renderBlock(po, event, Color.PINK);
                }
            }
        }
    }

    @EventHandler
    void onPreUpdate(PreUpdateEvent event) {
        pos = WorldUtil.findAllOres(mc.player.getBlockPos(), range.getValue().intValue());

        /*ticks = mc.player.isOnGround() ? 0 : ticks + 1;

        this.block = WorldUtil.findBlocks(
                mc.player.getBlockPos(),
                3

        );

        if (this.block != null && !mc.player.isOnGround()) {
            this.facing = WorldUtil.getClosest(this.block);
            if (this.facing != null && WorldUtil.canBePlacedOn(this.block)) {
                if (mc.world.getBlockState(mc.player.getBlockPos().down()).isAir() && ticks >= 9) {
                    mc.options.sprintKey.setPressed(false);
                    mc.player.setSprinting(false);
                    this.place(block, facing);
                    if (this.block != null) {
                        //ChatUtils.print(this.facing.asString());
                        this.rots = RotationUtils.getBlockRotations(this.block, this.facing);
                    }
                } else {
                    mc.player.setSprinting(true);
                    mc.options.sprintKey.setPressed(true);
                    rots = null;
                }
            }
        }*/
    }


    @EventHandler
    void onPreMotion(PreMotionEvent event) {


        if (this.rots != null) {
           // event.yaw = rots[0];
           // event.pitch = rots[1];
           // RotationUtils.turnHead(event.yaw);
        }
    }

    private void place(BlockPos pos, Direction direction) {
        //ChatUtils.print(direction.asString());
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