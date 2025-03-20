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

}
