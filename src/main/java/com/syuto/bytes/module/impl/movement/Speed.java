package com.syuto.bytes.module.impl.movement;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PreMotionEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.module.impl.combat.Killaura;
import com.syuto.bytes.setting.impl.ModeSetting;
import com.syuto.bytes.utils.impl.player.MovementUtil;
import com.syuto.bytes.utils.impl.player.PlayerUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

import static com.syuto.bytes.Byte.mc;

public class Speed extends Module {
    public ModeSetting modes = new ModeSetting(
            "Mode",
            this,
            "Watchdog",
            "Verus",
            "NCP"
    );

    public Speed() {
        super("Speed", "Zoom", Category.MOVEMENT);
    }


    @Override
    public void onEnable() {
        super.onEnable();

    }


    @EventHandler
    void onPreMotion(PreMotionEvent event) {
        mc.options.jumpKey.setPressed(false);


        switch (modes.getValue()) {
            case "Watchdog" -> {
                jump();
                if (mc.player.isOnGround()) {
                    setSpeed(0.42f);
                }
            }
            case "Verus" -> {

                setY(0.42f);
                setSpeed(0.35f);
                if (Killaura.target != null) {
                    setStafe(Killaura.target);
                }
            }
            case "NCP" -> {
                jump();
                setSpeed(0.285f);
            }

        }
    }


    void setStafe(Entity target) {
        double centerX = target.getX();
        double centerZ = target.getZ();
        double playerX = mc.player.getX();
        double playerZ = mc.player.getZ();

        double angle = Math.atan2(playerZ - centerZ, playerX - centerX) + Math.toRadians(45);

        double targetX = centerX + 1 * Math.cos(angle);
        double targetZ = centerZ + 1 * Math.sin(angle);

        double dx = targetX - playerX;
        double dz = targetZ - playerZ;

        double length = Math.sqrt(dx * dx + dz * dz);

        dx /= length;
        dz /= length;

        Vec3d movementInput = new Vec3d(dx * 0.5, mc.player.getVelocity().y, dz * 0.5);

        mc.player.setVelocity(movementInput);
    }


    private void jump() {
        if (mc.player.isOnGround()) {
            mc.player.jump();
        }
    }

    private void setY(double y) {
        Vec3d motion = mc.player.getVelocity();
        if (mc.player.isOnGround()) {
            mc.player.setVelocity(motion.x, y, motion.z);
        }
    }

    private void setSpeed(double speed) {
        if (MovementUtil.isMoving()) {
            MovementUtil.setSpeed(speed);
        }
    }


}
