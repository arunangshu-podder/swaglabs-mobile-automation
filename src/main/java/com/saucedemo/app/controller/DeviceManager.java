package com.saucedemo.app.controller;

import com.saucedemo.app.utils.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DeviceManager {
    /**
     * android: Launches the emulator by running the launch_emulator.sh script with the given device name.
     * ios: Launches the simulator by running the launch_simulator.sh script with the given device name.
     * @param deviceName the name of the device to launch
     * @throws IOException if the script fails to execute
     * @throws InterruptedException if the process is interrupted
     */
    public static void launchDevice(String deviceName) throws IOException, InterruptedException {
        if (Constants.ENABLE_PERFECTO) {
            LoggerManager.info("Device launching is not required for Perfecto testing. Skipping device launch.");
            return;
        }
        String platform = Constants.getPlatformName().toLowerCase();
        String scriptPath = getScriptPath(platform);
        ProcessBuilder processBuilder = new ProcessBuilder("bash", scriptPath, deviceName);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                LoggerManager.info(line);
            }
        }
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("Failed to launch device. Script exited with code: " + exitCode);
        }
    }

    /**
     * Returns the script path based on the platform.
     * Throws UnsupportedOperationException if the platform is not supported.
     *
     * @param platform the platform name (e.g., "android", "ios")
     * @return the path to the script for launching the device
     */
    private static String getScriptPath(String platform) {
        switch (platform.toLowerCase()) {
            case "android":
                return "src/main/resources/scripts/launch_emulator.sh";
            case "ios":
                return "src/main/resources/scripts/launch_simulator.sh";
            default:
                throw new UnsupportedOperationException("Device launching is only supported for Android and iOS platforms.");
        }
    }
}
