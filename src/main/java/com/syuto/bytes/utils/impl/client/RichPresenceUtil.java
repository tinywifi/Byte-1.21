package com.syuto.bytes.utils.impl.client;

import com.syuto.bytes.Byte;
import de.jcm.discordgamesdk.*;
import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.activity.ActivityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Objects;

import static com.syuto.bytes.Byte.mc;

public class RichPresenceUtil {
    private static Core core;
    private static Activity activity;
    private static long startTime;
    private static final Identifier resourceId = Identifier.of("byte", "libs/discord_game_sdk.dll");

    static long clientId = 1343623176751419443L;

    public static void init() {
        try {
            loadNativeLibrary();
            CreateParams params = new CreateParams();
            params.setClientID(clientId);
            core = new Core(params);
            activity = new Activity();

            startTime = System.currentTimeMillis() / 1000;

            updatePresence();
            System.out.println("Discord Rich Presence Initialized!");

            new Thread(() -> {
                while (true) {
                    try {
                        updatePresence();
                        core.runCallbacks();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updatePresence() {
        MinecraftClient client = MinecraftClient.getInstance();
        String details;
        String state;

        if (client.world != null) {
            if (client.getServer() != null) {
                details = "Hacking";
                state = "In Singleplayer";
            } else {
                ClientPlayNetworkHandler networkHandler = client.getNetworkHandler();
                if (networkHandler != null) {
                    String serverIP = networkHandler.getConnection().getAddress().toString();
                    state = "Playing on " + serverIP;
                } else {
                    state = "Playing Multiplayer";
                }
                details = "Exploring the World!";
            }
        } else {
            details = "In Menu";
            state = "Browsing Settings";
        }



        activity.setType(ActivityType.PLAYING);
        activity.setDetails(details);
        activity.setState(state);
        activity.assets().setLargeImage("large");
        activity.timestamps().setStart(Instant.ofEpochSecond(startTime));
        core.activityManager().updateActivity(activity);
    }

    public static void shutdown() {
        if (core != null) {
            core.close();
        }
    }



    private static void loadNativeLibrary() {
        try {
            System.load("C:\\Users\\malwarekat\\Documents\\Byte new\\src\\main\\resources\\assets\\byte\\libs\\discord_game_sdk.dll");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load Discord Game SDK native library!", e);
        }
    }

}
