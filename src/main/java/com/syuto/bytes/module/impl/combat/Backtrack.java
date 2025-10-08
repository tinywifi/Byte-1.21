package com.syuto.bytes.module.impl.combat;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PacketSentEvent;
import com.syuto.bytes.eventbus.impl.PreUpdateEvent;
import com.syuto.bytes.eventbus.impl.RenderWorldEvent;
import com.syuto.bytes.mixin.SendPacketMixinAccessor;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.ModuleManager;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.utils.impl.client.ChatUtils;
import com.syuto.bytes.utils.impl.render.RenderUtils;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class Backtrack extends Module {


    private ArrayList<Packet<?>> packetList = new ArrayList<>();

    private EntityHitResult entityHit;

    private boolean delay;

    private final long DELAY_TIME = 400L; //200 ms?

    private long time, last;

    private Vec3d pos, p;

    public Backtrack() {
        super("Backtrack", "Real asf reach", Category.COMBAT);
    }

    @Override
    public void onDisable() {
        clear(); //clear the packets
    }

    @EventHandler
    public void onPacketSent(PacketSentEvent event) {
        if (event.getPacket() instanceof PlayerInteractEntityC2SPacket)
            clear();

        if (delay) {
            synchronized (packetList) {
                packetList.add(event.getPacket());
                event.setCanceled(true);
            }
            //ChatUtils.print("Cancel");
        }
    }


    @EventHandler
    public void onPreUpdate(PreUpdateEvent event) {
        if (mc.world == null) return;

        HitResult hit = mc.crosshairTarget;
        if (hit.getType() == HitResult.Type.ENTITY) {
            entityHit = (EntityHitResult) hit;
        }

        long now = System.currentTimeMillis();

        if (!delay) {
            delay = true;
            last = now;
            pos = mc.player.getPos();
            ChatUtils.print("Delay started");
        } else if (now - last >= 600) {
            clear();
            p = mc.player.getPos();
            pos = null;
        }
    }



    private void clear() {
        SendPacketMixinAccessor silent = (SendPacketMixinAccessor) mc.getNetworkHandler();

        synchronized (packetList) {
            if (!packetList.isEmpty()) {
                for (Packet<?> packet : packetList) {
                    silent.getConnection().send(packet);
                }
                delay = false;
                packetList.clear();
                ChatUtils.print("Clear");
            }
        }
    }


    @EventHandler
    public void onRenderWorld(RenderWorldEvent e) {
        if ( pos  != null ) {
            RenderUtils.renderBox(pos, p, mc.player, e, e.partialTicks);
        }
    }
}