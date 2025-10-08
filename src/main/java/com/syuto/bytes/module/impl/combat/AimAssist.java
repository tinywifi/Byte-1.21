package com.syuto.bytes.module.impl.combat;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.*;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.setting.impl.NumberSetting;
import com.syuto.bytes.utils.impl.client.ChatUtils;
import com.syuto.bytes.utils.impl.player.PlayerUtil;
import com.syuto.bytes.utils.impl.render.RenderUtils;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AimAssist extends Module {

    public AimAssist() {
        super("Aim Assist", "Aims for you nigger.", Category.COMBAT);
    }

    private final NumberSetting delay = new NumberSetting("delay", this, 200, 0, 1000, 50);

    private boolean shouldSwitchBack = false;
    private long switchTime = 0;

    private boolean switchedThisTick = false;

    private final List<PlayerEntity> targets = new ArrayList<>();
    public static PlayerEntity target;

    @EventHandler
    public void onPreUpdate(PreUpdateEvent event) {
        target = null;
        targets.addAll(mc.world.getPlayers().stream()
                .filter(ent -> ent != mc.player)
                .sorted(Comparator.comparingDouble(PlayerUtil::getBiblicallyAccurateDistanceToEntity))
                .limit(4)
                .toList());

        for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
            if (PlayerUtil.getBiblicallyAccurateDistanceToEntity(player) <= 4 && player != mc.player) {
                target = player;
            }
        }

        if (target != null && mc.options.attackKey.isPressed() && PlayerUtil.isHoldingWeapon()) {
            mc.interactionManager.attackEntity(mc.player, target);
            mc.player.swingHand(mc.player.getActiveHand());

            //ChatUtils.print(target.getName());
        }


    }

    @EventHandler
    public void onRenderWorld(RenderWorldEvent e) {
        if (target != null) {
            RenderUtils.renderBox(target, e, e.partialTicks);
        }
    }

}
