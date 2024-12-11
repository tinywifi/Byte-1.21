package com.syuto.hwid.utils;

import java.util.HashMap;
import java.util.UUID;

public class ChallangeUtils {
    private static HashMap<String, String> activeChallenges = new HashMap<>();

    public static String generateChallenge(String username) {
        String challenge = UUID.randomUUID().toString();
        activeChallenges.put(username, challenge);
        return challenge;
    }

    public static String getChallenge(String username) {
        return activeChallenges.get(username);
    }

    public static void removeChallenge(String username) {
        activeChallenges.remove(username);
    }
}
