package com.syuto.bytes.module.impl.render;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.RenderTickEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.module.impl.render.clickgui.GuiHandler;
import com.syuto.bytes.module.impl.render.clickgui.ImGuiImpl;
import imgui.ImGui;
import org.lwjgl.glfw.GLFW;

import static com.syuto.bytes.Byte.mc;

public class ClickGui extends Module {
    public ClickGui() {
        super("ClickGui", "Click gui", Category.RENDER);
        //setKey(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.setScreen(null);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        mc.setScreen(new GuiHandler());
    }

}
