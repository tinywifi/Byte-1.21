package com.syuto.bytes.module.impl.movement;

import com.syuto.bytes.Byte;
import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.SlowDownEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.setting.impl.ModeSetting;

public class NoSlow extends Module {
    public ModeSetting modes = new ModeSetting("mode",this,"Vanilla", "Washdog");

    public NoSlow() {
        super("NoSlow", "Stops you from slowing down", Category.MOVEMENT);
        values.add(modes);
        setSuffix(() -> modes.getValue());
        Byte.LOGGER.info("UPDATE");
    }



    @EventHandler
    public void onSlowDown(SlowDownEvent event) {
        if (event.getMode().equals(SlowDownEvent.Mode.Item)) {
            event.setCanceled(true);
        }
    }
}
