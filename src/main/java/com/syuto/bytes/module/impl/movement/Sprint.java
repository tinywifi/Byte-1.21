package com.syuto.bytes.module.impl.movement;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PreUpdateEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import static com.syuto.bytes.Byte.mc;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", "Sprints for you", Category.MOVEMENT);
    }


    @EventHandler
    void onPreUpdate(PreUpdateEvent event) {
        mc.player.setSprinting(true);
        KeyBinding.setKeyPressed(mc.options.sprintKey.getDefaultKey(), true);
    }
}
