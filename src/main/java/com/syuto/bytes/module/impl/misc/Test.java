package com.syuto.bytes.module.impl.misc;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PreMotionEvent;
import com.syuto.bytes.eventbus.impl.PreUpdateEvent;
import com.syuto.bytes.eventbus.impl.RenderTickEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.utils.impl.rotation.RotationUtils;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.joml.Matrix4f;

import java.awt.*;

import static com.syuto.bytes.Byte.mc;

public class Test extends Module {

    public Test() {
        super("Test", "Module with absolutely 0 purpose.", Category.OTHER);
    }

    private boolean rot;
    float[] rots;
    Entity target;
    Color black = Color.BLACK;
    Color white = Color.WHITE;


    @EventHandler
    void onPreUpdate(PreUpdateEvent event) {
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof PlayerEntity && entity != mc.player) {
                if (mc.player.distanceTo(entity) <= 6.0) {
                    if (mc.player.getAttackCooldownProgress(0.5f) >= 1.0 && entity.isAlive()) {
                        target = entity;
                        mc.player.attack(entity);
                        mc.getNetworkHandler().sendPacket(PlayerInteractEntityC2SPacket.attack(entity, mc.player.isSneaking()));
                        mc.player.swingHand(Hand.MAIN_HAND);
                    }
                } else {
                    target = null;
                    rots = null;
                }
            }
        }
    }

    @EventHandler
    void onPreMotion(PreMotionEvent event) {
        if (target != null) {
            rots = RotationUtils.getRotationsToEntity(target);
            if (rots != null) {
                event.yaw = rots[0];
                event.pitch = rots[1];

            }
        }
    }

    @EventHandler
    public void onRenderTick(RenderTickEvent event) {
        VertexConsumerProvider vertexConsumers = mc.getBufferBuilders().getEntityVertexConsumers();
        Matrix4f matrix = new Matrix4f();
        OrderedText orderedText = Text.literal("</byte>").asOrderedText();

        mc.textRenderer.drawWithOutline(
                orderedText,
                5,
                10,
                white.getRGB(),
                black.getRGB(),
                matrix,
                vertexConsumers,
                255
        );
    }

}
