package top.infsky.cheatdetector.config;


import top.hendrixshen.magiclib.malilib.api.annotation.Config;
import top.hendrixshen.magiclib.malilib.api.annotation.Numeric;
import top.infsky.cheatdetector.config.utils.ConfigCategory;

public class Advanced2Config {
    @Config(category = ConfigCategory.ADVANCED2)
    public static boolean badPacket1Enabled = true;
    @Config(category = ConfigCategory.ADVANCED2)
    public static int badPacket1AlertBuffer = 20;

    @Config(category = ConfigCategory.ADVANCED2)
    public static boolean badPacket2Enabled = true;
    @Config(category = ConfigCategory.ADVANCED2)
    public static int badPacket2AlertBuffer = 20;

    @Config(category = ConfigCategory.ADVANCED2)
    public static boolean fastPlaceEnabled = true;
    @Config(category = ConfigCategory.ADVANCED2)
    public static int fastPlaceAlertBuffer = 100;
    @Config(category = ConfigCategory.ADVANCED2)
    public static int fastPlaceSamePlaceMinDelay = 1;

    @Config(category = ConfigCategory.ADVANCED2)
    public static boolean serverFreezeEnabled = true;
    @Config(category = ConfigCategory.ADVANCED2)
    public static int serverFreezeAlertBuffer = 1;
    @Numeric(minValue = -1, maxValue = 600)
    @Config(category = ConfigCategory.ADVANCED2)
    public static int serverFreezePostDelay = -1;
    @Numeric(minValue = 0, maxValue = 30000)
    @Config(category = ConfigCategory.ADVANCED2)
    public static int serverFreezeMaxMs = 800;
    @Config(category = ConfigCategory.ADVANCED2)
    public static boolean serverFreezeAutoDisableCheck = true;
    @Config(category = ConfigCategory.ADVANCED2)
    public static boolean serverFreezeAlert = true;

    @Config(category = ConfigCategory.ADVANCED2)
    public static boolean invalidYawEnabled = true;
    @Config(category = ConfigCategory.ADVANCED2)
    public static int invalidYawAlertBuffer = 1;
    @Config(category = ConfigCategory.ADVANCED2)
    public static double invalidYawMaxYaw = 360000;
}
