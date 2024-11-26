package com.syuto.bytes.module.impl.combat;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PreMotionEvent;
import com.syuto.bytes.eventbus.impl.PreUpdateEvent;
import com.syuto.bytes.eventbus.impl.RenderWorldEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.utils.impl.client.ChatUtils;
import com.syuto.bytes.utils.impl.rotation.RotationUtils;
import net.fabricmc.fabric.mixin.client.rendering.LivingEntityRendererAccessor;
import net.minecraft.client.model.Model;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.command.argument.RotationArgumentType;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.State;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.RotationCalculator;
import net.minecraft.util.math.RotationPropertyHelper;
import org.joml.Quaternionf;

import static com.syuto.bytes.Byte.mc;

public class Killaura extends Module {
    public Killaura() {
        super("Killaura", "Attacks people for you", Category.COMBAT);
    }

    private boolean rot;
    public static float[] rots;
    Entity target;

    @EventHandler
    void onPreUpdate(PreUpdateEvent event) {
        Entity closestEntity = null;

        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof LivingEntity && entity != mc.player) {
                double distance = mc.player.distanceTo(entity);
                if (distance <= 3.3 && entity.isAlive()) {
                    closestEntity = entity;
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

    //       mc.getCameraEntity().setBodyYaw(rots[0]);
    @EventHandler
    void onPreMotion(PreMotionEvent event) {
        if (target != null && rots != null) {
            event.yaw = rots[0];
            event.pitch = rots[1];
            //mc.player.changeLookDirection(rots[0], rots[1]);
        }
    }

    @EventHandler
    public void onRenderWorld(RenderWorldEvent e) {
        if (target != null) {
            rots = RotationUtils.getRotations(target);
        }
    }
}

