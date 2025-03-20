package com.syuto.bytes.module.impl.misc;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PreUpdateEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.utils.impl.client.ChatUtils;

public class Debugger extends Module {
    public Debugger() {
        super("Debugger", "Prints incoming packets", Category.EXPLOIT);
    }

    private int ticks;

    @EventHandler
    void onPreUpdate(PreUpdateEvent e) {

        ChatUtils.print(mc.player.getMovementSpeed());
    }

}
