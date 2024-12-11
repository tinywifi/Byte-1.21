package com.syuto.bytes.module.impl.player;

import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.setting.impl.NumberSetting;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;

public class FastPlace extends Module {

    public NumberSetting delay = new NumberSetting("Delay", this, 1, 0, 4, 1);

    public FastPlace() {
        super("FastPlace", "Modify right click timer.", Category.PLAYER);
        settings.add(delay);
    }

    public int getItemUseCooldown(ItemStack itemStack) {
        if (itemStack.getItem() instanceof BlockItem) {
            return delay.getValue().intValue();
        }
        return 4;
    }
}
