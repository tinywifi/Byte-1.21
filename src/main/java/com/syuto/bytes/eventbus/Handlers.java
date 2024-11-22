package com.syuto.bytes.eventbus;

import com.syuto.bytes.eventbus.impl.KeyEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.ModuleManager;
import org.lwjgl.glfw.GLFW;

public class Handlers {

    @EventHandler
    public void onKey(KeyEvent event) {
        if (event.getAction() == GLFW.GLFW_PRESS) {
            ModuleManager.modules.stream()
                    .filter(m -> m.getKey() == event.getKey())
                    .forEach(Module::toggle);
        }
    }

}