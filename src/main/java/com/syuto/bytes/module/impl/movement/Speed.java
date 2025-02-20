package com.syuto.bytes.module.impl.movement;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PreMotionEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.utils.impl.player.MovementUtil;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import javax.swing.text.html.parser.Entity;

public class Speed extends Module {
    public Speed() {
        super("Speed", "Zoom", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        ItemStack chestItem = mc.player.getEquippedStack(EquipmentSlot.CHEST);

        if (chestItem.isOf(Items.ELYTRA) && !mc.player.isGliding()) {
            mc.player.startGliding();

            Vec3d velocity = mc.player.getVelocity();
            mc.player.setVelocity(velocity.x, 0.05, velocity.z);
            mc.player.velocityDirty = true;

            mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
        }
    }


    @EventHandler
    void onPreMotion(PreMotionEvent event) {
        if (MovementUtil.isMoving()) {
            Vec3d velocity = mc.player.getVelocity();
            if (mc.player.isOnGround()) {
                mc.player.setVelocity(velocity.x, 0.3, velocity.z);
            }
            MovementUtil.setSpeed(0.42);
        }

        if (mc.player.isGliding()) {
            if (mc.options.jumpKey.isPressed()) {
                mc.player.setVelocity(0,5,0);
            } else {
                mc.player.setVelocity(0,0,0);
            }
            MovementUtil.setSpeed(0.42);
        }
    }


}
