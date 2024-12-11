package com.syuto.hwid;

import oshi.SystemInfo;
import oshi.hardware.*;

import java.security.MessageDigest;
import java.util.List;

public class Main {



    public static void main(String[] args) throws Exception {
        System.out.println("HWID: " + generateHWID());
    }






    public static String generateHWID() throws Exception {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        ComputerSystem computerSystem = hal.getComputerSystem();
        CentralProcessor processor = hal.getProcessor();
        List<HWDiskStore> storageList = hal.getDiskStores();
        List<NetworkIF> networkInterfaces = hal.getNetworkIFs();
        List<GraphicsCard> graphicsCards = hal.getGraphicsCards();

        String cpu = processor.getProcessorIdentifier().getName();
        String motherboard = computerSystem.getBaseboard().getSerialNumber();
        String diskSerial = storageList.isEmpty() ? "No disk found" : storageList.get(0).getSerial();
        String macAddress = networkInterfaces.isEmpty() ? "No MAC address" : networkInterfaces.get(0).getMacaddr();
        String gpu = graphicsCards.isEmpty() ? "No GPU found" : graphicsCards.get(0).getName();

        String combined = cpu + motherboard + diskSerial + macAddress + gpu;
        return hashString(combined.replaceAll("\\s+", ""));
    }


    public static String hashString(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes("UTF-8"));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
