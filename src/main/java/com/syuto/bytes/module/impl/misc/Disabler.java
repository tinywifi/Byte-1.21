package com.syuto.bytes.module.impl.misc;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PacketReceivedEvent;
import com.syuto.bytes.eventbus.impl.PacketSentEvent;
import com.syuto.bytes.eventbus.impl.PreMotionEvent;
import com.syuto.bytes.mixin.SendPacketMixinAccessor;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.utils.impl.client.ChatUtils;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldEventS2CPacket;
import net.minecraft.util.PlayerInput;

import java.util.Random;

public class Disabler extends Module {
    public Disabler() {
        super("Disabler", "Disables the ac", Category.EXPLOIT);
    }

    private int ticks = 0;


    @EventHandler
    void onPacketSent(PacketSentEvent event) {
        if (mc.player != null && mc.player.age <= 20) return;

        if (ticks == 0 && mc.player.age % 80 == 0) {
            ChatUtils.print("Disabler failed!");
        }


        if (ticks == 1) {
            ChatUtils.print("Disabler enabled!");
            ticks++;
        }

        if (ticks >= 3 && (ticks >= 328 || ticks <= 5) && event.getPacket() instanceof PlayerInteractEntityC2SPacket) {
            event.setCanceled(true);
        }

        SendPacketMixinAccessor silent = (SendPacketMixinAccessor) mc.getNetworkHandler();


        if (ticks >= 3) {
            if (event.getPacket() instanceof PlayerInputC2SPacket) {
                event.setCanceled(true);
                silent.getConnection().send(new PlayerInputC2SPacket(new PlayerInput(false, false, false, false, false, false, false)));
            }
            if ((ticks >= 328 || ticks <= 5) && event.getPacket() instanceof PlayerMoveC2SPacket) {
                event.setCanceled(true);
            } else if (ticks % 55 == 0) {
                silent.getConnection().send(new PlayerMoveC2SPacket.OnGroundOnly(true, mc.player.horizontalCollision));
            }
        }

        if (event.getPacket() instanceof CommonPongC2SPacket packet) {
            int parameter = packet.getParameter();
            if (parameter < 0 && (ticks >= 3 || ticks == 0)) {
                ticks++;
                silent.getConnection().send(new CommonPongC2SPacket(parameter + 1));
            } else if (parameter != 0) {
                event.setCanceled(true);
                if (ticks >= 330 || ticks == 2) {
                    silent.getConnection().send(new CommonPongC2SPacket(parameter + 5));
                    ticks = 3;
                }
            }
        }

    }


    @EventHandler
    void onPacketReceived(PacketReceivedEvent e) {
        if (e.getPacket() instanceof PlayerRespawnS2CPacket) {
            ticks = 0;
            ChatUtils.print("Reset");
        }
    }
}
