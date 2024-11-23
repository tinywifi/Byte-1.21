package com.syuto.bytes.mixin;


import com.syuto.bytes.Byte;
import com.syuto.bytes.eventbus.impl.ChatEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.encryption.NetworkEncryptionUtils;
import net.minecraft.network.message.LastSeenMessagesCollector;
import net.minecraft.network.message.MessageBody;
import net.minecraft.network.message.MessageChain;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.time.Instant;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ChatMixin extends ClientCommonNetworkHandler {

    protected ChatMixin(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
        super(client, connection, connectionState);
    }

    @Shadow private LastSeenMessagesCollector lastSeenMessagesCollector;

    @Shadow private MessageChain.Packer messagePacker;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void sendChatMessage(String content) {
        ChatEvent e = new ChatEvent(content);
        Byte.INSTANCE.eventBus.post(e);
        if (e.isCanceled()) return;

        System.out.println(content + " " + e.isCanceled());

        Instant instant = Instant.now();
        long l = NetworkEncryptionUtils.SecureRandomUtil.nextLong();
        LastSeenMessagesCollector.LastSeenMessages lastSeenMessages = this.lastSeenMessagesCollector.collect();
        MessageSignatureData messageSignatureData = this.messagePacker.pack(new MessageBody(content, instant, l, lastSeenMessages.lastSeen()));

        this.sendPacket(new ChatMessageC2SPacket(content, instant, l, messageSignatureData, lastSeenMessages.update()));
    }

}
