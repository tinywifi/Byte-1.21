package com.syuto.bytes.auth;

import com.syuto.bytes.utils.impl.hwid.HWIDUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Authentication {

    public static String sendRequest(String endpoint, String payload) throws Exception {
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(payload.getBytes("UTF-8"));
        }

        try (InputStream is = conn.getInputStream()) {
            return new String(is.readAllBytes(), "UTF-8");
        }
    }

    public static String authenticate(String username) throws Exception {
        String hwid = HWIDUtils.generateHWID();

        String payload = "{\"username\":\"" + username + "\",\"hwid\":\"" + hwid + "\"}";
        String challenge = sendRequest("http://localhost:42078/initiate", payload);

        String response = processChallenge(challenge, hwid);

        String responsePayload = "{\"username\":\"" + username + "\",\"response\":\"" + response + "\"}";
        String result = sendRequest("http://localhost:42078/validate", responsePayload);

        System.out.println("Authentication result: " + result);
        return result;
    }

    public static String processChallenge(String challenge, String hwid) throws Exception {
        String combined = challenge + hwid;
        return HWIDUtils.hashString(combined);
    }
}
