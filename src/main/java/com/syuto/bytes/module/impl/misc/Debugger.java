package com.syuto.bytes.module.impl.misc;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PacketSentEvent;
import com.syuto.bytes.eventbus.impl.PreUpdateEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.utils.impl.client.ChatUtils;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class Debugger extends Module {
    public Debugger() {
        super("Debugger", "Prints incoming packets", Category.OTHER);
    }

    private int ticks;

    @EventHandler
    void onPreUpdate(PreUpdateEvent e) {
         Vec3d pos = mc.player.getPos();
        if (mc.player.isUsingItem()) {
            for (int i = 0; i < 12; i++) {
                mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(pos.x, pos.y, pos.z, mc.player.getYaw(), mc.player.getPitch(), true, false));
            }
        }
    }

    @EventHandler
    void onPacketSent(PacketSentEvent event) {
        if (event.getPacket() instanceof ClickSlotC2SPacket e) {
            //ChatUtils.print("Received: " + e.getButton() + " slot: " + e.getSlot() + " " + ticks);
        }
    }
}
