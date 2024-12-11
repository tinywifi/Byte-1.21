package com.syuto.hwid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.syuto.hwid.utils.ChallangeUtils;
import com.syuto.hwid.utils.EncryptionUtil;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class Application {

    private static final String DATABASE_FILE = "users.json";
    private Map<String, Map<String, String>> database;

    public Application() {
        loadDatabase();
    }

    private void loadDatabase() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(DATABASE_FILE);
            if (file.exists()) {
                database = objectMapper.readValue(file, new TypeReference<Map<String, Map<String, String>>>() {});
            } else {
                database = Map.of();
                System.err.println("Database file not found! Using an empty database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load the database.");
        }
    }

    @PostMapping("/initiate")
    public String initiate(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String hwid = request.get("hwid");

        if (username == null || hwid == null || username.isEmpty() || hwid.isEmpty()) {
            return "INVALID_REQUEST";
        }

        Map<String, String> userRecord = database.values().stream()
                .filter(record -> record.get("username").equals(username))
                .findFirst()
                .orElse(null);

        if (userRecord == null || !userRecord.get("hwid").equals(hwid)) {
            System.out.println("User: " + username + " has a invalid hwid!");
            return "INVALID_HWID";
        }

        return ChallangeUtils.generateChallenge(username);
    }

    @PostMapping("/validate")
    public String validate(@RequestBody Map<String, String> request) throws Exception {
        String username = request.get("username");
        String clientResponse = request.get("response");

        if (username == null || clientResponse == null || username.isEmpty() || clientResponse.isEmpty()) {
            return "INVALID_REQUEST";
        }

        String challenge = ChallangeUtils.getChallenge(username);

        if (challenge == null) {
            return "NO_ACTIVE_CHALLENGE";
        }

        Map<String, String> userRecord = database.values().stream()
                .filter(record -> record.get("username").equals(username))
                .findFirst()
                .orElse(null);

        if (userRecord == null) {
            System.out.println("User: " + username + " is not found!");
            return "USER_NOT_FOUND";
        }

        String storedHWID = userRecord.get("hwid");

        String expectedResponse = EncryptionUtil.hashString(challenge + storedHWID);
        if (!expectedResponse.equals(clientResponse)) {
            System.out.println("User: " + username + " failed to authenticate!");
            return "AUTHENTICATION_FAILED";
        }

        ChallangeUtils.removeChallenge(username);

        System.out.println("User: " + username + " authenticated!");
        return "AUTHENTICATION_SUCCESS";
    }
}
