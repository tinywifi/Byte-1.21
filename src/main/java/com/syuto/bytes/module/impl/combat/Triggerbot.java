package com.syuto.bytes.module.impl.combat;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PreUpdateEvent;
import com.syuto.bytes.eventbus.impl.RenderWorldEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.utils.impl.client.ChatUtils;
import com.syuto.bytes.utils.impl.render.RenderUtils;
import com.syuto.bytes.utils.impl.rotation.RotationUtils;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class Triggerbot extends Module {
    public Triggerbot() {
        super("Triggerbot", "Attacks for you while over a tagret", Category.COMBAT);
    }

    public EntityHitResult entityHit;


    @EventHandler
    void onPreUpdate(PreUpdateEvent event) {
        HitResult hit = mc.crosshairTarget;

        if (hit.getType() == HitResult.Type.ENTITY) {
            entityHit = (EntityHitResult) hit;


            if (mc.player.getAttackCooldownProgress(0.5f) >= 1.0) {
                mc.interactionManager.attackEntity(mc.player, entityHit.getEntity());
                mc.player.swingHand(Hand.MAIN_HAND);
            }

        }

    }

    @EventHandler
    public void onRenderWorld(RenderWorldEvent e) {
        if (entityHit != null && entityHit.getEntity().isAlive()) {
            RenderUtils.renderBox(entityHit.getEntity(), e, e.partialTicks);
        }
    }
}
