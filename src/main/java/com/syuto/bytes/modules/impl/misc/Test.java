package com.syuto.bytes.modules.impl.misc;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PreUpdateEvent;
import com.syuto.bytes.eventbus.impl.RenderTickEvent;
import com.syuto.bytes.eventbus.impl.RenderWorldEvent;
import com.syuto.bytes.modules.Module;
import com.syuto.bytes.utils.impl.ChatUtils;
import com.syuto.bytes.utils.impl.render.RenderUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.InteractionEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

import static com.syuto.bytes.Byte.mc;

public class Test extends Module {

    public Test() {
        super("Test", GLFW.GLFW_KEY_F25, "hi");
    }

    Color black = Color.BLACK;
    Color white = Color.WHITE;


    @EventHandler
    void onPreUpdate(PreUpdateEvent event) {
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof SkeletonEntity) {
                if (mc.player.distanceTo(entity) <= 6.0) {
                    if (mc.player.getAttackCooldownProgress(0.5f) >= 1.0 && entity.isAlive()) {
                        mc.player.attack(entity);
                        mc.getNetworkHandler().sendPacket(PlayerInteractEntityC2SPacket.attack(entity, mc.player.isSneaking()));
                        mc.player.swingHand(Hand.MAIN_HAND);
                    }
                }
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
