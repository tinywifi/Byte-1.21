package com.syuto.bytes.modules.impl.player;

import com.syuto.bytes.modules.Module;
import com.syuto.bytes.modules.ModuleManager;
import com.syuto.bytes.modules.settings.impl.IntSetting;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;

public class FastPlace extends Module {

    public static IntSetting delay = new IntSetting("Delay", 1,0,4);
    public FastPlace() {
        super("FastPlace", GLFW.GLFW_KEY_F25, "hi");
    }

    public static int getItemUseCooldown(ItemStack itemStack) {
        if (ModuleManager.getModule("FastPlace").isEnabled() && itemStack.getItem() instanceof BlockItem) {
            return delay.getValue();
        }
        return 4;
    }
}
