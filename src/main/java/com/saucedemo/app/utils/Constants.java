package com.saucedemo.app.utils;

import static com.saucedemo.app.utils.PropertyUtils.Config;

public interface Constants {
    // Android app details
    public static final String APK_FILE_PATH = Config.getProperty("apk.file.path");
    public static final String APP_PACKAGE = Config.getProperty("apk.app.package");
    public static final String APP_ACTIVITY = Config.getProperty("apk.app.activity");
    // iOS app details
    public static final String APP_FILE_PATH = Config.getProperty("app.file.path");
    public static final String BUNDLE_ID = Config.getProperty("app.bundle.id");
    // Appium Server details
    public static final String APPIUM_IP_ADDRESS = Config.getProperty("appium.ip.address");
    public static final int APPIUM_PORT = Integer.parseInt(Config.getProperty("appium.port"));
    public static final String APPIUM_JS_FILEPATH = Config.getProperty("appium.js.path");
    public static final String APPIUM_NODE_EXECUTABLE = Config.getProperty("appium.node.path");
    // Device details
    public static final boolean IS_APP_PRE_INSTALLED = Boolean.parseBoolean(Config.getProperty("is.app.preinstalled"));
    public static final String DEVICE_PROFILE = Config.getProperty("device.profile");
    public static String getPlatformName() { return JsonUtils.getString("platform_name"); }
    public static String getPlatformVersion() { return JsonUtils.getString("platform_version"); }
    public static String getDeviceName() { return JsonUtils.getString("device_name"); }
    public static String getAutomationName() { return JsonUtils.getString("automation_name"); }
    // Perfecto details
    public static boolean ENABLE_PERFECTO = Boolean.parseBoolean(Config.getProperty("enable.perfecto"));
    interface Perfecto {
        public static final String PERFECTO_URL = Config.getProperty("perfecto.url");
        public static final String PERFECTO_TOKEN = Config.getProperty("perfecto.token");
        public static String getPlatformBuild() { return JsonUtils.getString("platform_build"); }
        public static String getManufacturer() { return JsonUtils.getString("manufacturer"); }
        public static String getResolution() { return JsonUtils.getString("resolution"); }
        public static boolean getAutoLaunch() { return Boolean.parseBoolean(JsonUtils.getString("auto_launch")); }
        public static boolean enableAppiumBehavior() { return Boolean.parseBoolean(JsonUtils.getString("enable_appium_behavior")); }
    }
}