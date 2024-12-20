package com.syuto.bytes.module.impl.render;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.PreUpdateEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;

public class RenderTest extends Module {
    public RenderTest() {
        super("RenderTest", "test", Category.RENDER);
    }


    @EventHandler
    void onPreUpdate(PreUpdateEvent e) {
    }
}
