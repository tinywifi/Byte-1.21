package com.syuto.bytes.module.impl.render;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PreMotionEvent;
import com.syuto.bytes.eventbus.impl.PreUpdateEvent;
import com.syuto.bytes.eventbus.impl.RenderTickEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.utils.impl.client.ChatUtils;
import com.syuto.bytes.utils.impl.render.RenderUtils;
import net.fabricmc.fabric.api.client.particle.v1.ParticleRenderEvents;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.HotbarStorage;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientTickEndC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerLoadedC2SPacket;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class RenderTest extends Module {
    public RenderTest() {
        super("RenderTest", "test", Category.RENDER);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler
    void onRenderTick(RenderTickEvent event) {
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof PlayerEntity en && entity.isAlive() && entity != mc.player) {
                if (!isEntityInView(en)) continue;
                float delta = mc.getRenderTickCounter().getTickDelta(true);

                double interpolatedX = en.prevX + (en.getX() - en.prevX) * delta;
                double interpolatedY = en.prevY + (en.getY() - en.prevY) * delta;
                double interpolatedZ = en.prevZ + (en.getZ() - en.prevZ) * delta;

                Box interpolatedBox = en.getBoundingBox().expand(0.15f).offset(
                        interpolatedX - en.getX(),
                        interpolatedY - en.getY(),
                        interpolatedZ - en.getZ()
                );

                Vec3d[] corners = {
                        new Vec3d(interpolatedBox.minX, interpolatedBox.minY, interpolatedBox.minZ),
                        new Vec3d(interpolatedBox.maxX, interpolatedBox.minY, interpolatedBox.minZ),
                        new Vec3d(interpolatedBox.minX, interpolatedBox.maxY, interpolatedBox.minZ),
                        new Vec3d(interpolatedBox.maxX, interpolatedBox.maxY, interpolatedBox.minZ),
                        new Vec3d(interpolatedBox.minX, interpolatedBox.minY, interpolatedBox.maxZ),
                        new Vec3d(interpolatedBox.maxX, interpolatedBox.minY, interpolatedBox.maxZ),
                        new Vec3d(interpolatedBox.minX, interpolatedBox.maxY, interpolatedBox.maxZ),
                        new Vec3d(interpolatedBox.maxX, interpolatedBox.maxY, interpolatedBox.maxZ),
                };

                float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE;
                float maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE;

                for (Vec3d corner : corners) {
                    Vec3d screenPos = RenderUtils.worldToScreen(corner);
                    if (screenPos != null) {
                        minX = Math.min(minX, (float) screenPos.x);
                        minY = Math.min(minY, (float) screenPos.y);
                        maxX = Math.max(maxX, (float) screenPos.x);
                        maxY = Math.max(maxY, (float) screenPos.y);
                    }
                }

                if (minX < maxX && minY < maxY) {
                    RenderUtils.drawRectOutline(event, minX, minY, maxX, maxY, 0xFFFFFFFF);
                    //RenderUtils.drawRect(event, minX - 0.1F, minY + 0.1F, maxX + 0.1F, maxY - 0.1F, 0x80000000);
                }
            }
        }
    }



    public boolean isEntityInView(Entity entity) {
        Vec3d playerLook = mc.player.getRotationVec(1.0F);
        Vec3d toEntity = entity.getPos().subtract(mc.player.getPos()).normalize();
        double angle = Math.acos(playerLook.dotProduct(toEntity));
        return Math.toDegrees(angle) < mc.options.getFov().getValue() / 1.5f;
    }
}
