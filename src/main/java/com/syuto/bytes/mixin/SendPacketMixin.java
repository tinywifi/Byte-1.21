package com.syuto.bytes.mixin;

import com.syuto.bytes.Byte;
import com.syuto.bytes.eventbus.impl.PacketSentEvent;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonNetworkHandler.class)
public class SendPacketMixin {

    @Inject(at = @At("HEAD"), method = "sendPacket", cancellable = true)
    public void packetEvent(Packet<?> packet, CallbackInfo ci) {
        PacketSentEvent event = new PacketSentEvent(packet);
        Byte.INSTANCE.eventBus.post(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

}