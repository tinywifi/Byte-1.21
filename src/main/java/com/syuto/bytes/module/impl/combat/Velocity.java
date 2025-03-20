package com.syuto.bytes.module.impl.combat;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PacketReceivedEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.setting.impl.BooleanSetting;
import com.syuto.bytes.setting.impl.NumberSetting;
import com.syuto.bytes.utils.impl.client.ChatUtils;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.util.math.Vec3d;

import static com.syuto.bytes.Byte.mc;

public class Velocity extends Module {
    public NumberSetting horizontal = new NumberSetting("H", this,100,0, 100, 1);
    public NumberSetting vertical = new NumberSetting("V", this,100,0, 100, 1);
    public BooleanSetting explosion = new BooleanSetting("Explosions",this,false);

    public Velocity() {
        super("Velocity", "Anti knockback", Category.COMBAT);
    }



    @EventHandler
    public void onPacketReceived(PacketReceivedEvent event) {
        if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket s12) {
            if (s12.getEntityId() == mc.player.getId()) {
                double horizontal = this.horizontal.getValue().doubleValue() / 100.0, vertical = this.vertical.getValue().doubleValue() / 100.0;

                double x = horizontal == 0 ? mc.player.getVelocity().x : (s12.getVelocityX()) * horizontal;
                double y = vertical == 0 ? mc.player.getVelocity().y : (s12.getVelocityY()) * vertical;
                double z = horizontal == 0 ? mc.player.getVelocity().z : (s12.getVelocityZ()) * horizontal;

                event.setCanceled(true);

                mc.player.setVelocity(x,y,z);
            }
        }
    }
}
