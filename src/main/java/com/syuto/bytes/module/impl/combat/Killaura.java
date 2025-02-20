package com.syuto.bytes.module.impl.combat;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.*;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.setting.impl.ModeSetting;
import com.syuto.bytes.setting.impl.NumberSetting;
import com.syuto.bytes.utils.impl.client.ChatUtils;
import com.syuto.bytes.utils.impl.player.MovementUtil;
import com.syuto.bytes.utils.impl.player.PlayerUtil;
import com.syuto.bytes.utils.impl.render.AnimationUtils;
import com.syuto.bytes.utils.impl.render.RenderUtils;
import com.syuto.bytes.utils.impl.rotation.RotationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class Killaura extends Module {

    private final ModeSetting targeting = new ModeSetting("Target Mode", this, "Single", "Switch");
    private final NumberSetting delay = new NumberSetting("Switch delay", this, 200, 0, 1000, 50);
    private final NumberSetting aps = new NumberSetting("APS", this, 10, 1, 20, 1);
    private final NumberSetting reach = new NumberSetting("Reach", this, 6, 1, 8, 0.5);
    private final NumberSetting swing = new NumberSetting("Swing range", this, 6, 1, 8, 0.5);
    private final ModeSetting autoBlock = new ModeSetting("Autoblock", this, "None", "Watchdog", "Fake", "Vanilla");

    private long lastSwitchTime, attackDelay, lastAttackTime;
    private final List<PlayerEntity> targets = new ArrayList<>();
    private PlayerEntity target;
    private float[] rotations, lastRotation;
    private boolean shouldAttack, firstBlock, blocking;
    private int targetIndex = 0, ticks;

    public Killaura() {
        super("Killaura", "Attacks people for you.", Category.COMBAT);
        this.setSuffix(targeting::getValue);
    }

    @Override
    public void onDisable() {
        if (this.blocking) {
            mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
            this.blocking = false;
            ChatUtils.print("Unblocked on post prolly this will ban at some point xD.");
        }
        this.target = null;
        this.ticks = 0;
        this.lastSwitchTime = 0;
        this.firstBlock = false;
        this.lastRotation = null;
        this.lastAttackTime = 0;
        AnimationUtils.setBlocking(false);
    }

    @EventHandler
    public void onPreUpdate(PreUpdateEvent event) {
        if (lastRotation == null) {
            lastRotation = new float[]{mc.player.getYaw(), mc.player.getPitch()};
        }

        if (targeting.getValue().equals("Single")) {
            updateSingleTarget();
        } else {
            updateSwitchTarget();
        }

        if (rotations != null) {
            rotations = RotationUtils.getFixedRotation(rotations, lastRotation);
        }

        if (target != null && rotations != null) {
            EntityHitResult result = (EntityHitResult) PlayerUtil.raycast(rotations[0], rotations[1], swing.getValue().doubleValue(), delta, false);
            ticks++;
            if (result != null && result.getEntity().equals(this.target)) {
                handleAutoBlock();
                executeAttack(result);
            }
        } else {
            if (blocking) {
                mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));;
                this.blocking = false;
                ChatUtils.print("Unblocked");
            }
        }

        if (autoBlock.getValue() != "None") {
            AnimationUtils.setBlocking(target != null);
        }
    }

    @EventHandler
    public void onPreMotion(PreMotionEvent event) {
        if (rotations != null) {
            float[] move = MovementUtil.move(rotations[0]);

        }
        if (rotations != null && target != null) {
            event.yaw = rotations[0];
            event.pitch = rotations[1];
            RotationUtils.turnHead(event.yaw);

            this.lastRotation = this.rotations;
        }
    }

    @EventHandler
    void onPacketSent(PacketSentEvent event) {
        if (event.getPacket() instanceof PlayerActionC2SPacket e) {
            if (target != null && e.getAction() == PlayerActionC2SPacket.Action.RELEASE_USE_ITEM) {
                ChatUtils.print("C07 " + e.getAction() + " " + ticks);
                event.setCanceled(true);
            }
        }
    }

    @EventHandler
    public void onRenderTick(RenderTickEvent e) {
        if (target != null && System.currentTimeMillis() - lastAttackTime >= attackDelay && canSwing(target)) {
            lastAttackTime = System.currentTimeMillis();
            updateAttackDelay();
            shouldAttack = true;
        }

        if (target != null) {
            rotations = RotationUtils.getRotations(lastRotation, mc.player.getEyePos(), target);
        }
    }

    @EventHandler
    public void onRenderWorld(RenderWorldEvent e) {
        if (target != null && canAttack(target)) {
            RenderUtils.renderBox(target, e, e.partialTicks);
        }
    }

    private void updateSwitchTarget() {
        if (System.currentTimeMillis() - lastSwitchTime < delay.getValue().longValue()) return;
        lastSwitchTime = System.currentTimeMillis();

        targets.clear();
        targets.addAll(mc.world.getPlayers().stream()
                .filter(ent -> ent != mc.player && canAttack(ent))
                .sorted(Comparator.comparingDouble(PlayerEntity::getHealth))
                .limit(4)
                .toList());

        if (!targets.isEmpty()) {
            targetIndex = (targetIndex + 1) % targets.size();
            target = targets.get(targetIndex);
        } else {
            target = null;
        }
    }

    private void updateSingleTarget() {
        target = mc.world.getPlayers().stream()
                .filter(this::canAttack)
                .min(Comparator.comparingDouble(PlayerEntity::getHealth))
                .orElse(null);
    }

    private boolean canAttack(PlayerEntity entity) {
        double distance = PlayerUtil.getBiblicallyAccurateDistanceToEntity(entity);
        return entity != mc.player && distance <= swing.getValue().doubleValue() && entity.isAlive();
    }

    private boolean canSwing(PlayerEntity entity) {
        double distance = PlayerUtil.getBiblicallyAccurateDistanceToEntity(entity);
        return entity != mc.player && distance <= reach.getValue().doubleValue() && entity.isAlive();
    }

    private void updateAttackDelay() {
        attackDelay = (long) (1000.0 / aps.getValue().longValue());
    }

    private void handleAutoBlock() {
        if (autoBlock.getValue().equals("Watchdog")) {
            if (!firstBlock) {
                swapSlots();
                block(rotations[0], rotations[1]);
                ChatUtils.print("First Nigger");
                firstBlock = true;
            } else if (ticks % 8 == 0) {
                swapSlots();
                block(rotations[0], rotations[1]);
                ChatUtils.print("Nigger");
            }
        }
    }

    private void executeAttack(EntityHitResult r) {
        if (canSwing(target) && mc.player.getAttackCooldownProgress(0.5f) >= 1.0) {
            mc.interactionManager.attackEntity(mc.player, target);
            mc.getNetworkHandler().sendPacket(
                    PlayerInteractEntityC2SPacket.interactAt(
                            r.getEntity(),
                            mc.player.isSneaking(),
                            Hand.MAIN_HAND,
                            new Vec3d(0, 0, 0)
                    )
            );
            mc.interactionManager.interactEntity(mc.player, target, Hand.MAIN_HAND);
            mc.player.swingHand(mc.player.getActiveHand());
            shouldAttack = false;
        }
    }

    private void swapSlots() {
        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket((mc.player.getInventory().selectedSlot % 7) + 2));
        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
        blocking = false;
    }

    private void block(float yaw, float pitch) {
        mc.getNetworkHandler().sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, -1, yaw, pitch));
        blocking = true;
    }
}
