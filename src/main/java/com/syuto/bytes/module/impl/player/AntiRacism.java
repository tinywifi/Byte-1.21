package com.syuto.bytes.module.impl.player;

import com.syuto.bytes.eventbus.EventHandler;
import com.syuto.bytes.eventbus.impl.ChatEvent;
import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;


public class AntiRacism extends Module {
    public AntiRacism() {
        super("Disabler", "FUCK RACISM", Category.PLAYER);
    }

    static String[] racismList = new String[] {
            "fuck",
            "nigger",
            "nigga",
            "negroid",
            "niglet",
            "niggle",
            "monkey",
            "charcoal chimpanzee",
            "shit",
            "ass",
            "cunt",
            "sket",
            "bitch",
            "bastard",
            "retard",
            "skid",
            "nignog",
            "niggur",
            "blackie",
            "tranny",
            "faggot",
            "ladyboy"
    };

    @EventHandler
    public void onChat(ChatEvent event) {
        String message = event.getMessage().toLowerCase();
        for (String word : racismList) {
            if (message.contains(word)) {
                String replacement = "*".repeat(word.length());
                event.setMessage(message.replaceAll(word, replacement));
            }
        }
    }
}

