package com.syuto.bytes.module.impl.combat;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PacketReceivedEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.util.math.Vec3d;

import static com.syuto.bytes.Byte.mc;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity", "Anti knockback", Category.COMBAT);
    }


    @EventHandler
    public void onPacketReceived(PacketReceivedEvent event) {
        if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket) {
            EntityVelocityUpdateS2CPacket s12 = (EntityVelocityUpdateS2CPacket) event.getPacket();
            if (s12.getEntityId() == mc.player.getId()) {
                Vec3d velo = mc.player.getVelocity();
                mc.player.setVelocity(velo.x, s12.getVelocityY(), velo.z);
                event.setCanceled(true);
            }
        }
    }
}
