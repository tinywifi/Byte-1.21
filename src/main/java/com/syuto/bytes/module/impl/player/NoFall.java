package com.syuto.bytes.module.impl.player;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PreMotionEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.setting.impl.ModeSetting;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {

    public ModeSetting modes = new ModeSetting("mode",this,"Packet", "Spoof", "NoGround");

    public NoFall() {
        super("NoFall", "Stops fall damage", Category.PLAYER);
        setSuffix(() -> modes.getValue());
    }

    @EventHandler
    public void onPreMotion(PreMotionEvent event) {
        boolean ground = mc.player.isOnGround();

        switch (modes.getValue()) {
            case "Packet" -> {
                if (!ground) {
                    double x = mc.player.getX();
                    double y = mc.player.getY();
                    double z = mc.player.getZ();
                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, true, mc.player.horizontalCollision));
                }
            }
            case "Spoof" -> {
                event.onGround = true;
            }

            case "NoGround" -> {
                event.onGround = false;
            }
        }
    }
}
