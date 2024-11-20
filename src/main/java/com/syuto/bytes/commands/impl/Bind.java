package com.syuto.bytes.commands.impl;

import com.syuto.bytes.commands.Command;
import com.syuto.bytes.modules.Module;
import com.syuto.bytes.modules.ModuleManager;
import com.syuto.bytes.utils.impl.ChatUtils;
import com.syuto.bytes.utils.impl.keyboard.KeyboardUtil;
import org.lwjgl.glfw.GLFW;


public class Bind extends Command {
    public Bind() {
        super("Bind", "Binds a module by name.", ".bind <module> <key> | <clear> | <list>", "b");
    }

    @Override
    public void onCommand(String[] args, String message) {
        if (args.length == 2) {
            Module module = ModuleManager.getModule(args[0]);
            String keyname = args[1].toUpperCase();

            if (module != null) {
                module.clearKeys();
                if (keyname.equals("NONE")) {
                    module.addKey(GLFW.GLFW_KEY_F25);
                    ChatUtils.print("Unbound " + module.getName() + ".");
                } else {
                    int keyCode = KeyboardUtil.stringToGlfwKey(keyname); //Keyboard.getKeyIndex(keyname)
                    if (keyCode == -1) {
                        ChatUtils.print("Invalid key: " + keyname);
                    } else {
                        module.addKey(keyCode);
                        ChatUtils.print("Bound " + module.getName() + " to " + keyname + ".");
                    }
                }
                return;
            }
            ChatUtils.print("Module \"" + args[0] + "\" was not found.");
            return;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("clear")) {
                ModuleManager.getModules().values().forEach(Module::clearKeys);
                ChatUtils.print("Cleared all binds.");
                return;
            } else if (args[0].equalsIgnoreCase("list")) {
                listBinds();
                return;
            }
        }

        sendSyntaxHelpMessage();
    }

    private void listBinds() {
        int bound = 0;
        for (Module module : ModuleManager.getModules().values()) {
            if (!module.getKeys().isEmpty()) {
                bound++;
            }
        }

        if (bound > 0) {
            ChatUtils.print("Current binds: ");
            for (Module module : ModuleManager.getModules().values()) {
                if (!module.getKeys().isEmpty()) {
                    ChatUtils.print(module.getName() + ": " + module.getKeysString());
                }
            }
        } else {
            ChatUtils.print("No modules are currently bound.");
        }
    }
}
