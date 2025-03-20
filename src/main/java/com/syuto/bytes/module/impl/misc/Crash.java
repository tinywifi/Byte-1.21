package com.syuto.bytes.module.impl.misc;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.TickEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.utils.impl.client.ClientUtil;

public class Crash extends Module {
    public Crash() {
        super("Crash", "Memory Corruption as a feature", Category.EXPLOIT);
    }

    @EventHandler
    void onTick(TickEvent e) {
        ClientUtil.crash();
    }
}
