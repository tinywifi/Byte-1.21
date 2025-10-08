package com.syuto.bytes.commands.impl;

import com.syuto.bytes.commands.Command;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.ModuleManager;
import com.syuto.bytes.module.impl.player.PathFind;
import com.syuto.bytes.utils.impl.client.ChatUtils;
import com.syuto.bytes.utils.impl.keyboard.KeyboardUtil;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class Pathfind extends Command {

    public Pathfind() {
        super("Pathfind", "pathfind to coord", ".pathfind <x> <y> <z>", "p");
    }

    @Override
    public void onCommand(String[] args, String message) {
        if (args.length == 3) {
            try {

                PathFind path = ModuleManager.getModule(PathFind.class);

                if (path != null) {
                    double x = Double.parseDouble(args[0]);
                    double y = Double.parseDouble(args[1]);
                    double z = Double.parseDouble(args[2]);

                    path.startPathfinding(x, y, z);
                    ChatUtils.print("Pathfinding to: " + x + ", " + y + ", " + z);
                    return;
                }
            } catch (NumberFormatException e) {
                ChatUtils.print("Invalid coordinates: must be numbers.");
                return;
            }
        }

        sendSyntaxHelpMessage();
    }
}
