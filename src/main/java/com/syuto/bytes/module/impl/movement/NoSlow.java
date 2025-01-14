package com.syuto.bytes.module.impl.movement;

import com.syuto.bytes.Byte;
import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PreMotionEvent;
import com.syuto.bytes.eventbus.impl.SlowDownEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.setting.impl.ModeSetting;
import com.syuto.bytes.utils.impl.client.ChatUtils;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.network.packet.c2s.handshake.ConnectionIntent;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;

public class NoSlow extends Module {
    public ModeSetting modes = new ModeSetting("mode",this,"Vanilla", "Washdog");

    public NoSlow() {
        super("NoSlow", "Stops you from slowing down", Category.MOVEMENT);
        setSuffix(() -> modes.getValue());
    }

    @EventHandler
    public void onSlowDown(SlowDownEvent event) {
        if (event.getMode().equals(SlowDownEvent.Mode.Item)) {
            event.setCanceled(true);
        }
    }

    @EventHandler
    void onPreMotion(PreMotionEvent event) {
        if (mc.player.isUsingItem() && !(mc.player.getInventory().getStack(mc.player.getInventory().selectedSlot).getItem() instanceof SwordItem)) {
            event.posY += 0.000001;

        }
    }

    //grim  mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 36, 1, SlotActionType.SWAP, mc.player);
}

