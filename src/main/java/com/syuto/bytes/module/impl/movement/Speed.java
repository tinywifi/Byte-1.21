package com.syuto.bytes.module.impl.movement;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PreMotionEvent;
import com.syuto.bytes.eventbus.impl.PreUpdateEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.module.impl.combat.Killaura;
import com.syuto.bytes.setting.impl.ModeSetting;
import com.syuto.bytes.setting.impl.NumberSetting;
import com.syuto.bytes.utils.impl.client.ChatUtils;
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

import java.util.Objects;
import java.util.Random;
import java.util.function.BooleanSupplier;

import static com.syuto.bytes.Byte.mc;

public class Speed extends Module {

    public ModeSetting modes = new ModeSetting(
            "Mode",
            this,
            "Watchdog",
            "Verus",
            "NCP",
            "Custom"
    );

    private BooleanSupplier view = () -> modes.getValue().equals("Custom");


    public NumberSetting speed = new NumberSetting(
            "sped",
            this,
            view,
            1,
            0,
            8,
            0.01
    );

    public Speed() {
        super("Speed", "Zoom", Category.MOVEMENT);
        setSuffix(() -> modes.getValue());
    }


    private int ground = 0;


    @Override
    public void onEnable() {
        super.onEnable();

    }

    @EventHandler
    public void onPreUpdate(PreUpdateEvent event) {
        view = () -> modes.getValue().equals("Custom");
    }


    @EventHandler
    void onPreMotion(PreMotionEvent event) {
        boolean ground = mc.player.isOnGround();

        this.ground = !ground ? this.ground + 1 : 0;

        //mc.options.jumpKey.setPressed(false);


        switch (modes.getValue()) {
            case "Watchdog" -> {
                jump();
                setSpeed(2f);
            }
            case "Verus" -> {
                if (mc.player.isOnGround()) {
                    if (mc.player.horizontalCollision) {
                        setMotY(0.42f);
                    } else {
                        setMotY(0.05);
                    }
                }

                switch(this.ground) {

                    case 1 -> {
                        if (!mc.player.horizontalCollision) {
                            setMotY(-0.8f);
                        }
                    }

                    case 3 -> {
                        setMotY(-0.3f);
                    }


                }
                setSpeed(1f);
                if (Killaura.target != null && mc.options.jumpKey.isPressed()) {
                    setStafe(Killaura.target);
                }
            }
            case "NCP" -> {
                jump();
                switch(this.ground) {
                }
                setSpeed(0.26f);
            }

            case "Custom" -> {
                jump();
                setSpeed(speed.getValue().doubleValue());
            }

        }
    }


    void setStafe(Entity target) {
        double centerX = target.getX();
        double centerZ = target.getZ();
        double playerX = mc.player.getX();
        double playerZ = mc.player.getZ();

        double angle = Math.atan2(playerZ - centerZ, playerX - centerX) + Math.toRadians(45);

        double targetX = centerX + 1.5 * Math.cos(angle);
        double targetZ = centerZ + 1.5 * Math.sin(angle);

        double dx = targetX - playerX;
        double dz = targetZ - playerZ;

        double length = Math.sqrt(dx * dx + dz * dz);

        dx /= length;
        dz /= length;

        Vec3d movementInput = new Vec3d(dx, mc.player.getVelocity().y, dz);

        mc.player.setVelocity(movementInput);
    }


    private void jump() {
        if (mc.player.isOnGround() && MovementUtil.isMoving()) {
            mc.player.jump();
        }
    }

    private void setY(double y) {
        Vec3d motion = mc.player.getVelocity();
        if (mc.player.isOnGround()) {
            mc.player.setVelocity(motion.x, y, motion.z);
        }
    }

    private void setMotY(double y) {
        Vec3d motion = mc.player.getVelocity();
        mc.player.setVelocity(motion.x, y, motion.z);
    }


    private void setSpeed(double speed) {
        if (MovementUtil.isMoving()) {
            MovementUtil.setSpeed(speed);
        }
    }


}
