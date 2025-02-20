package com.syuto.bytes.module.impl.combat;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PreMotionEvent;
import com.syuto.bytes.eventbus.impl.RenderTickEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.utils.impl.player.PlayerUtil;
import com.syuto.bytes.utils.impl.player.WorldUtil;
import com.syuto.bytes.utils.impl.rotation.RotationUtils;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Unique;

public class AimAssist extends Module {

    public AimAssist() {
        super("Aim Assist", "Aims for you nigger.", Category.COMBAT);
    }

    private float[] rots, last;
    private Entity closestEntity;


    @EventHandler
    void onPreMotion(PreMotionEvent e) {
        if (last == null) {
            last = new float[]{mc.player.getYaw(), mc.player.getPitch()};
        }
        closestEntity = null;
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof PlayerEntity livingEntity && entity != mc.player) {
                double distance = PlayerUtil.getBiblicallyAccurateDistanceToEntity(entity);
                if (! WorldUtil.isOnTeam(livingEntity) && distance <= 3.5 && livingEntity.isAlive()) {
                    closestEntity = entity;
                }
            }
        }

        if (closestEntity != null) {
            if (rots != null) {
                rots = RotationUtils.getFixedRotation(rots, last);
            }

        } else {
            last = null;
            rots = null;
        }

    }


    @EventHandler
    void onRenderTick(RenderTickEvent e) {
        if (last != null && closestEntity != null) {
            rots = RotationUtils.getRotations(last, mc.player.getEyePos(), closestEntity);
        }


        if (rots != null && mc.options.attackKey.isPressed()) {
            mc.player.setYaw(MathHelper.lerpAngleDegrees(0.05f, last[0], rots[0]));
            mc.player.setPitch(MathHelper.lerpAngleDegrees(0.05f, last[1], rots[1]));

            last = new float[]{mc.player.getYaw(), mc.player.getPitch()};
        }

    }

}
