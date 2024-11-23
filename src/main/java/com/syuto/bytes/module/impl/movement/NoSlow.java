package com.syuto.bytes.module.impl.movement;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.SlowDownEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.setting.impl.ModeSetting;

import static com.syuto.bytes.Byte.mc;

public class NoSlow extends Module {
    public ModeSetting modes = new ModeSetting("mode",this,"Vanilla");

    public NoSlow() {
        super("NoSlow", "Stops you from slowing down", Category.MOVEMENT);
    }



    @EventHandler
    public void onSlowDown(SlowDownEvent event) {
        if (event.getMode().equals(SlowDownEvent.Mode.Item)) {
            event.setCanceled(true);
        }
    }
}
