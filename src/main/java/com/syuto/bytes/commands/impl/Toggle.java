package com.syuto.bytes.commands.impl;

import com.syuto.bytes.commands.Command;
import com.syuto.bytes.modules.Module;
import com.syuto.bytes.modules.ModuleManager;
import com.syuto.bytes.utils.impl.ChatUtils;
import net.minecraft.network.message.SentMessage;
import net.minecraft.util.Formatting;

import static com.syuto.bytes.Byte.mc;


public class Toggle extends Command {
    public Toggle() {
        super("toggle", "Toggles a module.", ".toggle <module>", "t");
    }

    @Override
    public void onCommand(String[] args, String message) {
        if (args.length < 1) {
            ChatUtils.print("§bCorrect usage:§7 " + getSyntax());
            return;
        }

        String modName = args[0];
        Module m = ModuleManager.getModule(modName);

        if (m == null) {
            ChatUtils.print("Module name " +  Formatting.RED  + modName + Formatting.RED + " is invalid");
            return;
        }

        if (!m.isEnabled()) {
            ModuleManager.toggleModule(m);
            ChatUtils.print("" + m.getName() + Formatting.GRAY + " was " + Formatting.GREEN + "enabled");
        } else {
            ModuleManager.toggleModule(m);
            ChatUtils.print("" + m.getName() + Formatting.GRAY + " was " + Formatting.RED + "disabled");
        }
    }
}
