package com.syuto.bytes.module.impl.render;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.RenderTickEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.utils.impl.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

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
            if (!(entity instanceof PlayerEntity en) || !isEntityInView(en) || !en.isAlive() || en == mc.player)
                continue;

            int h = 0;
            double x = en.prevX + (en.getX() - en.prevX) * delta;
            double y = en.prevY + (en.getY() - en.prevY) * delta + en.getHeight() + 0.8;
            double z = en.prevZ + (en.getZ() - en.prevZ) * delta;

            Vec3d worldPos = new Vec3d(x, y, z);
            Vec3d screenPos = RenderUtils.worldToScreen(worldPos);


            MatrixStack matrices = event.context.getMatrices();
            String name = en.getName().getString() + " " + String.format("%.1f", en.getHealth()) + "HP";
            int nameWidth = mc.textRenderer.getWidth(name);
            int nameHeight = mc.textRenderer.fontHeight;

            matrices.push();
            matrices.translate(screenPos.x, screenPos.y, 0);
            matrices.scale(1.0f, 1.0f, 1.0f);

            float xx = -nameWidth / 2f;
            float yy = 0;

            RenderUtils.drawRect(
                    event.context,
                    xx + nameWidth /2,
                    yy + nameHeight / 2,
                    nameWidth + 2,
                    nameHeight + 2,
                    new Color(0,0,0,125).getRGB()
            );


            RenderUtils.drawText(
                    event.context,
                    name,
                    -nameWidth / 2f,
                    0,
                    Color.WHITE.getRGB()
            );

            int itemSpacing = 15;
            List<ItemStack> armor = new ArrayList<>();
            armor.add(en.getMainHandStack());
            armor.add(en.getOffHandStack());

            en.getArmorItems().forEach(armor::add);


            Collections.reverse(armor);

            int totalWidth = armor.size() * itemSpacing;
            int startX = (int) (xx + (nameWidth / 2f) - (totalWidth / 2f));

            for (ItemStack item : armor) {
                event.context.drawItem(item, startX, -20);
                startX += itemSpacing;
            }
            matrices.pop();
        }
    }




    public boolean isEntityInView(Entity entity) {
        Entity cameraEntity = mc.getCameraEntity();
        if (cameraEntity == null) return false;
        Vec3d cameraLook = cameraEntity.getRotationVec(1.0F).normalize();
        Vec3d toEntity = entity.getPos()
                .add(0, entity.getStandingEyeHeight(), 0)
                .subtract(cameraEntity.getCameraPosVec(1.0F))
                .normalize();

        double dot = cameraLook.dotProduct(toEntity);

        double fov = mc.options.getFov().getValue();
        double fovRadians = Math.toRadians(fov);
        double threshold = Math.cos(fovRadians);

        return dot > threshold;
    }
}
