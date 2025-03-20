package com.syuto.bytes.module.impl.player;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PreMotionEvent;
import com.syuto.bytes.eventbus.impl.RenderWorldEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.utils.impl.client.ChatUtils;
import com.syuto.bytes.utils.impl.player.InventoryUtil;
import com.syuto.bytes.utils.impl.player.PlayerUtil;
import com.syuto.bytes.utils.impl.player.WorldUtil;
import com.syuto.bytes.utils.impl.render.RenderUtils;
import com.syuto.bytes.utils.impl.rotation.RotationUtils;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;


public class BedAura extends Module {
    public BedAura() {
        super("BedAura", "bedaura", Category.PLAYER);
    }

    BlockPos blockPos;
    BlockPos secondBlockPos;
    BlockPos fastestBlock;
    boolean mining = false;
    int ticks;
    float[] rots;
    float progress;

    @EventHandler
    void onPreUpdate(PreMotionEvent e) {
        if (!mining) {
            ChatUtils.print("Finding bed");
            blockPos = findBed();
            if (blockPos != null) {
                ChatUtils.print("Found bed checking if open");
                blockPos = isOpen(blockPos);
                if (blockPos == null) {
                    ChatUtils.print("Bed is not open finding outside");
                    blockPos = findClosestBlockOutside();
                }
            }
        }


        if (blockPos != null) {
            ChatUtils.print("Found bed and is open");
            Direction facing = WorldUtil.getClosest(blockPos);
            int slot = InventoryUtil.getBestHotbarSlotToBreakBlock(
                    mc.world.getBlockState(blockPos).getBlock()
            );

            float speed = PlayerUtil.calcBlockBreakingDelta(blockPos, mc.player.getInventory().getStack(slot));

            ticks++;


            if (facing != null) {
                rots = RotationUtils.getBlockRotations(blockPos, facing);

                if (progress == 0) {
                    mc.player.getInventory().selectedSlot = InventoryUtil.getBestHotbarSlotToBreakBlock(
                            mc.world.getBlockState(blockPos).getBlock()
                    );

                    e.yaw = rots[0];
                    e.pitch = rots[1];
                    RotationUtils.turnHead(e.yaw);
                    ChatUtils.print("Start mine");
                    mc.getNetworkHandler().sendPacket(
                            new PlayerActionC2SPacket(
                                    PlayerActionC2SPacket.Action.START_DESTROY_BLOCK,
                                    blockPos,
                                    facing
                            )
                    );

                    mc.player.swingHand(mc.player.getActiveHand());
                    mining = true;
                }

                if (mining) {
                    progress += speed;
                    mc.player.swingHand(mc.player.getActiveHand());
                    if (progress > 1.0f) {
                        e.yaw = rots[0];
                        e.pitch = rots[1];
                        RotationUtils.turnHead(e.yaw);
                        ChatUtils.print("End mine");
                        mc.getNetworkHandler().sendPacket(
                                new PlayerActionC2SPacket(
                                        PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK,
                                        blockPos,
                                        facing
                                )
                        );
                        progress = 0;
                        mining = false;
                        blockPos = null;
                    }
                }
            } else {
                ChatUtils.print("facing is null why the fuck!");
            }



        }
    }

    @EventHandler
    void onRenderWorld(RenderWorldEvent event) {
        if (blockPos != null) {
            RenderUtils.renderBlock(blockPos, event, event.partialTicks);
        }
    }

    private BlockPos findBed() {
        BlockPos block = WorldUtil.findBlocksAround(
                mc.player.getBlockPos(),
                6
        );

        if (isBed(block))
            return block;

        return null;
    }

    private BlockPos isOpen(BlockPos position) {
        BlockState blockState = mc.world.getBlockState(position);
        secondBlockPos = position.offset(BedBlock.getOppositePartDirection(blockState));

        for (Direction adjacent : Direction.values()) {
            if (adjacent.equals(Direction.DOWN)) continue;
            if (isAir(position.offset(adjacent)) || isAir(secondBlockPos.offset(adjacent))) {
                return position;
            }
        }
        return null;
    }

    private BlockPos findClosestBlockOutside() {
        blockPos = findBed();

        BlockPos[] surroundingBlocks = {
                blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.up(),
                secondBlockPos.north(), secondBlockPos.south(), secondBlockPos.east(), secondBlockPos.west(), secondBlockPos.up()
        };

        BlockPos bestBlock = null;
        double bestMiningTime = Double.MAX_VALUE;

        for (BlockPos pos : surroundingBlocks) {
            if (mc.world.getBlockState(pos).isLiquid()) {
                return blockPos;
            }

            if (!isAir(pos) && !isBed(pos)) {
                float miningTime = getMiningTime(pos);
                if (miningTime < bestMiningTime) {
                    bestMiningTime = miningTime;
                    bestBlock = pos;
                }
            }
        }
        return bestBlock;
    }


    private float getMiningTime(BlockPos pos) {
        if (pos != null && !isBed(pos)) {
            float hardness = mc.world.getBlockState(pos).getBlock().getHardness();
            float breakSpeed = mc.player.getInventory().getBlockBreakingSpeed(mc.world.getBlockState(pos));

            return hardness / breakSpeed;
        }

        return 0.0f;

    }

    private boolean isBed(BlockPos block) {
        return block != null && mc.world.getBlockState(block).getBlock() instanceof BedBlock;
    }

    private boolean isAir(BlockPos pos) {
        return pos != null && mc.world.getBlockState(pos).isAir();
    }
}
