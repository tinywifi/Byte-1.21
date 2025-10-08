package com.syuto.bytes.mixin;

import com.syuto.bytes.Byte;
import com.syuto.bytes.eventbus.impl.WorldJoinEvent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.syuto.bytes.Byte.mc;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayerNetworkHandlerMixin {

    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void onWorldJoin(GameJoinS2CPacket packet, CallbackInfo info) {

        if (mc.world != null) {
            WorldJoinEvent event = new WorldJoinEvent(packet.playerEntityId());
            Byte.INSTANCE.eventBus.post(event);
        }
    }
}
