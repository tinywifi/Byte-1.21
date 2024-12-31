package com.syuto.bytes.module.impl.combat;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PacketSentEvent;
import com.syuto.bytes.eventbus.impl.PreMotionEvent;
import com.syuto.bytes.eventbus.impl.PreUpdateEvent;
import com.syuto.bytes.eventbus.impl.RenderWorldEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.setting.impl.NumberSetting;
import com.syuto.bytes.utils.impl.player.PlayerUtil;
import com.syuto.bytes.utils.impl.render.RenderUtils;
import com.syuto.bytes.utils.impl.rotation.RotationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsage;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;

public class Killaura extends Module {
    public NumberSetting reach = new NumberSetting("Reach",this,3.0D,3.0D,6.0D, 0.5D);

    public Killaura() {
        super("Killaura", "Attacks people for you", Category.COMBAT);
        this.setSuffix(() -> "Switch");
    }

    private boolean rot;
    public static float[] rots;
    Entity target;
    int ticks, d;

    @EventHandler
    void onPreUpdate(PreUpdateEvent event) {
        Entity closestEntity = null;
        double minHealth = Double.MAX_VALUE;

        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof PlayerEntity && entity != mc.player) {
                double distance = PlayerUtil.getBiblicallyAccurateDistanceToPlayer(entity);
                PlayerEntity livingEntity = (PlayerEntity) entity;
                if (distance <= reach.getValue().doubleValue() && livingEntity.isAlive()) {
                    if (livingEntity.getHealth() < minHealth) {
                        closestEntity = entity;
                        minHealth = livingEntity.getHealth();
                    }
                }
            }
        }

        if (closestEntity != null) {
            ticks++;
            this.target = closestEntity;
            ItemUsage.consumeHeldItem(mc.world,mc.player,Hand.MAIN_HAND);

            if (ticks % 1 == 0) {
                //mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(8));
                //mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(0));
                mc.interactionManager.interactItem(mc.player,Hand.MAIN_HAND);
                if (target.isAlive()) {
                    mc.interactionManager.attackEntity(mc.player, target);
                    mc.player.swingHand(Hand.MAIN_HAND);
                }
            }
        } else {
            this.target = null;
            rots = null;
        }
    }


    @EventHandler
    void onPreMotion(PreMotionEvent event) {
        if (target != null && rots != null) {
            float yaw = Math.clamp(rots[0] % 360.0F, -360.0F, 360.0F);
            float pitch = Math.clamp(rots[1] % 360.0F, -90.0F, 90.0F);;

            event.yaw = yaw;
            event.pitch = pitch;

            mc.player.headYaw = yaw;

            mc.player.bodyYaw = yaw;

        }
    }

    @EventHandler
    public void onRenderWorld(RenderWorldEvent e) {
        if (target != null) {
            RenderUtils.renderBox(target, e, e.partialTicks);
            rots = RotationUtils.getRotations(target);
        }
    }


    @EventHandler
    void onPacketSent(PacketSentEvent event) {
        if (event.getPacket() instanceof PlayerActionC2SPacket) {
            PlayerActionC2SPacket c07 = (PlayerActionC2SPacket) event.getPacket();
            event.setCanceled(true);
        }

    }
}

