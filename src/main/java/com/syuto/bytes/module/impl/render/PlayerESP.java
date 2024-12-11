package com.syuto.bytes.module.impl.render;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.RenderWorldEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.utils.impl.render.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.player.PlayerEntity;


import java.awt.*;

import static com.syuto.bytes.Byte.mc;

public class PlayerESP extends Module {
    public PlayerESP() {
        super("PlayerESP", "Shows players through walls", Category.RENDER);
    }

    @EventHandler
    public void onRenderWorld(RenderWorldEvent event) {
        for (Entity e : mc.world.getEntities()) {
            if (e instanceof PlayerEntity) {
                if (e.isAlive() && e != mc.player) {
                    float delta = mc.getRenderTickCounter().getTickDelta(true);
                    LivingEntity en = (LivingEntity) e;

                    //RenderUtils.drawCircle(en, event, 0.5f, Color.red.getRGB(), delta);
                    RenderUtils.renderHealth(en, event, en.getHealth(), en.getMaxHealth(), (en.getHealth() / en.getMaxHealth()), delta);
                    //RenderUtils.renderBox(e,event,delta); //3d esp
                }
            }
        }
    }
}
