package com.syuto.bytes.module.impl.render;

import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import dev.blend.ui.dropdown.DropdownClickGUI;
import org.lwjgl.glfw.GLFW;

public class ClickGUIModule extends Module {

    public ClickGUIModule() {
        super("ClickGUI", "Displays a GUI for the user to configure the client in.", Category.RENDER);
        setKey(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @Override
    protected void onEnable() {
        mc.setScreen(DropdownClickGUI.INSTANCE);
    }

    @Override
    protected void onDisable() {
        if (mc.currentScreen instanceof DropdownClickGUI) {
            mc.setScreen(null);
        }
    }

}
