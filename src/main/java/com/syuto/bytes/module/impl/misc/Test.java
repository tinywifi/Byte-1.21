package com.syuto.bytes.module.impl.misc;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PreUpdateEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.ModuleManager;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.module.impl.combat.Velocity;
import com.syuto.bytes.utils.impl.client.ChatUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import static com.syuto.bytes.Byte.mc;


public class Test extends Module {

    public Test() {
        super("Test", "Module with absolutely 0 purpose.", Category.OTHER);
    }



    @EventHandler
    public void onPreUpdate(PreUpdateEvent e) {
        Velocity velo = ModuleManager.getModule(Velocity.class);
        assert velo != null;

        do {
            ChatUtils.print("Hello");
        } while (velo.isEnabled());
    }


}
