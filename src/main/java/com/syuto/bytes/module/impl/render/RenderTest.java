package com.syuto.bytes.module.impl.render;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.RenderTickEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.utils.impl.render.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
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
        float delta = mc.getRenderTickCounter().getTickDelta(false);
        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof PlayerEntity en) || !en.isAlive() || !isEntityInView(en) || en == mc.player) continue;

            double interpolatedX = en.prevX + (en.getX() - en.prevX) * delta;
            double interpolatedY = en.prevY + (en.getY() - en.prevY) * delta;
            double interpolatedZ = en.prevZ + (en.getZ() - en.prevZ) * delta;

            float expansion = 0.1f;
            Box interpolatedBox = en.getBoundingBox().expand(expansion).offset(
                    interpolatedX - en.getX(),
                    interpolatedY - en.getY(),
                    interpolatedZ - en.getZ()
            );

            Vec3d[] corners = new Vec3d[8];
            for (int i = 0; i < 8; i++) {
                double x = (i & 1) == 0 ? interpolatedBox.minX : interpolatedBox.maxX;
                double y = (i & 2) == 0 ? interpolatedBox.minY : interpolatedBox.maxY;
                double z = (i & 4) == 0 ? interpolatedBox.minZ : interpolatedBox.maxZ;
                corners[i] = new Vec3d(x, y, z);
            }

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
                RenderUtils.drawRectOutline(event.context.getMatrices(), minX, maxX, maxY, minY, 0xFFFFFFFF);
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
