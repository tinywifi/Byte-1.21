package com.syuto.bytes.module.impl.misc;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PacketSentEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.utils.impl.client.ChatUtils;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;


public class Test extends Module {

    public Test() {
        super("Test", "Module with absolutely 0 purpose.", Category.OTHER);
    }

    @EventHandler
    public void onPacketSent(PacketSentEvent event) {
        if (event.getPacket() instanceof PlayerInteractEntityC2SPacket) {
            event.setCanceled(true);
        }
        //ChatUtils.print(event.getPacket().getPacketId());
    }

}
