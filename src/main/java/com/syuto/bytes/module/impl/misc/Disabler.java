package com.syuto.bytes.module.impl.misc;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PacketSentEvent;
import com.syuto.bytes.eventbus.impl.PreUpdateEvent;
import com.syuto.bytes.eventbus.impl.WorldJoinEvent;
import com.syuto.bytes.mixin.SendPacketMixinAccessor;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.setting.impl.ModeSetting;
import com.syuto.bytes.utils.impl.client.ChatUtils;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;

import java.util.ArrayList;

public class Disabler extends Module {
    public ModeSetting modes = new ModeSetting("mode",this,"Vulcan", "CubeCraft");
    public Disabler() {
        super("Disabler", "Disables the ac", Category.EXPLOIT);
        setSuffix(() -> modes.getValue());
    }

    private boolean accept;
    private int ticks, id;
    private ArrayList<Packet<?>> packetList = new ArrayList<>();

    @Override
    public void onEnable() {
        reset();
        ChatUtils.print("To use this wait for io.netty.handler.timeout.ReadTimeoutException and it's disabled");
    }

    @EventHandler
    public void onWorldJoin(WorldJoinEvent event) {
        if (modes.getValue().equals("Vulcan")) {
            if (event.getEntityId() == mc.player.getId()) {
                accept = true;
                ticks = 0;
                ChatUtils.print("Disabling..");
            }
        } else if (modes.getValue().equals("CubeCraft")) {
            if (event.getEntityId() == mc.player.getId()) {
                id = -20; //starting id of grim
            }
        }

    }

    @EventHandler
    public void onPacketSent(PacketSentEvent event) {
        if (modes.getValue().equals("Vulcan")) {
            if (event.getPacket() instanceof CommonPongC2SPacket) {
                if (accept) {
                    ChatUtils.print("Cancel " + ticks);
                    event.setCanceled(true);
                }
            }
        } else if (modes.getValue().equals("CubeCraft")) {
            if (event.getPacket() instanceof CommonPongC2SPacket c0f) {
                if (c0f.getParameter() > 0) {
                    CommonPongC2SPacket a = new CommonPongC2SPacket((int) (Math.random() * 100000));
                    packetList.add(a);
                    //ChatUtils.print("CC " + a.getParameter());
                    event.setCanceled(true);
                }
            }
        }
    }


    @EventHandler
    public void onPreUpdate(PreUpdateEvent event) {
        if (modes.getValue().equals("Vulcan")) {
            if (accept) ticks++;

            if (ticks >= 1000) {
                ChatUtils.print("if you didn't get disconnected relog.");
                reset();
            }
        } else if (modes.getValue().equals("CubeCraft")) {
            if (!packetList.isEmpty()) ticks++;

            if (ticks % 100 == 0) {
                clear();
            }
        }
    }

    private void reset() {
        ticks = 0;
        accept = false;
    }


    private void clear() {
        SendPacketMixinAccessor silent = (SendPacketMixinAccessor) mc.getNetworkHandler();

        synchronized (packetList) {
            if (!packetList.isEmpty()) {
                for (Packet<?> packet : packetList) {
                    silent.getConnection().send(packet);
                }
                packetList.clear();
                ChatUtils.print("Clear");
            }
        }
    }

}

