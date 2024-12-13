package com.syuto.bytes.module.impl.combat;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PreMotionEvent;
import com.syuto.bytes.eventbus.impl.PreUpdateEvent;
import com.syuto.bytes.eventbus.impl.RenderWorldEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.setting.impl.NumberSetting;
import com.syuto.bytes.utils.impl.render.RenderUtils;
import com.syuto.bytes.utils.impl.rotation.RotationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

public class Killaura extends Module {
    public NumberSetting reach = new NumberSetting("Reach",this,3.0D,3.0D,6.0D, 0.5D);

    public Killaura() {
        super("Killaura", "Attacks people for you", Category.COMBAT);
        this.setSuffix("Switch");
//        values.add(reach); // this isn't needed. do NOT do it.
    }

    private boolean rot;
    public static float[] rots;
    Entity target;

    @EventHandler
    void onPreUpdate(PreUpdateEvent event) {
        Entity closestEntity = null;
        double minHealth = Double.MAX_VALUE;

        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof LivingEntity && entity != mc.player) {
                double distance = mc.player.distanceTo(entity);
                LivingEntity livingEntity = (LivingEntity) entity;
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
            if (mc.player.getAttackCooldownProgress(0.5f) >= 1.0 && target.isAlive()) {
                mc.interactionManager.attackEntity(mc.player, target);
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        } else {
            this.target = null;
            rots = null;
        }
    }


    @EventHandler
    void onPreMotion(PreMotionEvent event) {
        if (target != null && rots != null) {
            event.yaw = rots[0];
            event.pitch = rots[1];
            mc.player.bodyYaw = MathHelper.wrapDegrees(rots[0]);
            mc.player.headYaw = MathHelper.wrapDegrees(rots[0]);

        }
    }

    @EventHandler
    public void onRenderWorld(RenderWorldEvent e) {
        if (target != null) {
            RenderUtils.renderBox(target, e, e.partialTicks);
            rots = RotationUtils.getRotations(target);
        }
    }
}

