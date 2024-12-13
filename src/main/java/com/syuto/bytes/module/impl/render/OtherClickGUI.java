package com.syuto.bytes.module.impl.render;

import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import dev.blend.ui.dropdown.DropdownClickGUI;
import org.lwjgl.glfw.GLFW;

public class OtherClickGUI extends Module {

    public OtherClickGUI() {
        super("ClikGU!", "description", Category.RENDER);
        setKey(GLFW.GLFW_KEY_RIGHT_CONTROL);
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
