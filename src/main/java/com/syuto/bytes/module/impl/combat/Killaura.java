package com.syuto.bytes.module.impl.combat;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.*;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.setting.impl.ModeSetting;
import com.syuto.bytes.setting.impl.NumberSetting;
import com.syuto.bytes.utils.impl.client.ChatUtils;
import com.syuto.bytes.utils.impl.player.PlayerUtil;
import com.syuto.bytes.utils.impl.render.RenderUtils;
import com.syuto.bytes.utils.impl.rotation.RotationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import static com.syuto.bytes.Byte.mc;

public class Killaura extends Module {
    public NumberSetting aps = new NumberSetting("APS",this,10,1,20, 1);
    public NumberSetting reach = new NumberSetting("Reach",this,3.0D,3.0D,6.0D, 0.5D);
    public ModeSetting abs = new ModeSetting("Autoblock",this,"None", "Watchdog", "Vanilla");

    public Killaura() {
        super("Killaura", "Attacks people for you", Category.COMBAT);
        this.setSuffix(() -> "Switch");
    }

    private boolean blocking, shouldAttack;
    public static float[] rots, last;
    Entity target;
    int ticks, d;

    private long delay, lastTime;

    @EventHandler
    void onPreUpdate(PreUpdateEvent event) {
        Entity closestEntity = null;
        double minHealth = Double.MAX_VALUE;

        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof PlayerEntity livingEntity && entity != mc.player) {
                double distance = PlayerUtil.getBiblicallyAccurateDistanceToEntity(entity);
                if (distance <= reach.getValue().doubleValue() && livingEntity.isAlive()) {
                    if (livingEntity.getHealth() < minHealth) {
                        closestEntity = entity;
                        minHealth = livingEntity.getHealth();
                    }
                }
            }
        }

        if (closestEntity != null) {
            this.target = closestEntity;
            ticks++;

            switch(abs.value) {
                case "Vanilla" -> {
                    mc.getNetworkHandler().sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, -1, mc.player.getYaw(), mc.player.getPitch()));
                }

                case "Watchdog" -> {

                    if (ticks % 8 == 0) {
                        if (blocking) {
                            mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot % 7 + 2));
                            mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
                            blocking = false;
                            ChatUtils.print("Unblock");
                        }

                        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                        ChatUtils.print("block");
                        blocking = true;
                    }

                }
            }


            if (shouldAttack) {
                if (target.isAlive()) {
                    mc.interactionManager.interactEntity(mc.player, target, Hand.MAIN_HAND);
                    mc.getNetworkHandler().sendPacket(PlayerInteractEntityC2SPacket.attack(target, mc.player.isSneaking()));
                    //mc.interactionManager.attackEntity(mc.player, target);
                    mc.player.swingHand(Hand.MAIN_HAND);
                }
                shouldAttack = false;
            }

        } else {
            this.target = null;
            rots = null;
        }

        if (blocking && target == null) {
            mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
            blocking = false;
        }
    }


    @EventHandler
    void onPreMotion(PreMotionEvent event) {
        if (target != null && rots != null) {
            if (last == null) {
                last = rots;
            }

            rots = RotationUtils.getFixedRotation(rots, last);

            event.yaw = rots[0];
            event.pitch = rots[1];

            float f = MathHelper.wrapDegrees(event.yaw - mc.player.bodyYaw);

            mc.player.bodyYaw += f * 0.3F;
            mc.player.headYaw = event.yaw;
            if (Math.abs(f) > 50.0f) {
                mc.player.bodyYaw += f - (float) MathHelper.sign(f) * 50.0f;
            }

            last = rots;

        }
    }

    @EventHandler
    void onRenderTick(RenderTickEvent e) {
        if (System.currentTimeMillis() - this.lastTime >= delay && target != null) {
            this.lastTime = System.currentTimeMillis();
            updateAttackDelay();
            if (target != null) {
                shouldAttack = true;
            }
        }
    }

    @EventHandler
    public void onRenderWorld(RenderWorldEvent e) {
        if (target != null) {
            RenderUtils.renderBox(target, e, e.partialTicks);
            rots = RotationUtils.getRotations(mc.player.getEyePos(), target);

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

    private void updateAttackDelay() {
        delay = (long)(1000.0 / aps.value.longValue());
    }
}

