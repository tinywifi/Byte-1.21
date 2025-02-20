package com.syuto.bytes.module.impl.player;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PacketReceivedEvent;
import com.syuto.bytes.eventbus.impl.PacketSentEvent;
import com.syuto.bytes.eventbus.impl.PreMotionEvent;
import com.syuto.bytes.eventbus.impl.PreUpdateEvent;
import com.syuto.bytes.mixin.SendPacketMixin;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.utils.impl.client.ChatUtils;
import com.syuto.bytes.utils.impl.player.MovementUtil;
import com.syuto.bytes.utils.impl.render.RenderUtils;
import com.syuto.bytes.utils.impl.rotation.RotationUtils;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedList;
import java.util.Queue;

public class Blink extends Module {
    private int ticks;
    Vec3d velo;
    private Queue<Packet<?>> packetList = new LinkedList<>();

    public Blink() {
        super("FakeLag", "Lags your packets!", Category.PLAYER);
    }

    @EventHandler
    void onPreUpdate(PreMotionEvent e) {
        e.yaw = MovementUtil.direction();
        RotationUtils.turnHead(e.yaw);
    }

    @EventHandler
    void onPacketSent(PacketSentEvent event) {
    }

    @EventHandler
    public void onPacketReceived(PacketReceivedEvent event) {

    }
}