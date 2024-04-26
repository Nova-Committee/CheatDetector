package top.infsky.cheatdetector.config;

import top.hendrixshen.magiclib.malilib.api.annotation.Config;
import top.hendrixshen.magiclib.malilib.api.annotation.Numeric;

public class Advanced3Config {
    @Config(category = ConfigCategory.ADVANCED3)
    public static int flagDetectorAlertBuffer = 1;
    @Config(category = ConfigCategory.ADVANCED3)
    public static int flagDetectorWorldChangedDisableTick = 60;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean flagDetectorShouldReduceKnownTeleport = true;

    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean spinDoSpinYaw = true;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean spinDoSpinPitch = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean spinAllowBadPitch = false;
    @Numeric(minValue = -180, maxValue = 180)
    @Config(category = ConfigCategory.ADVANCED3)
    public static double spinDefaultYaw = 90;
    @Numeric(minValue = -180, maxValue = 180)
    @Config(category = ConfigCategory.ADVANCED3)
    public static double spinDefaultPitch = -90;
    @Config(category = ConfigCategory.ADVANCED3)
    public static double spinYawStep = 45;
    @Config(category = ConfigCategory.ADVANCED3)
    public static double spinPitchStep = 35;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean spinOnlyPacket = false;

    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean blinkIncludeOutgoing = true;
//    @Config(category = ConfigCategory.ADVANCED3)
//    public static boolean blinkIncludeInComing = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean blinkAutoDisable = false;
    @Numeric(minValue = -1, maxValue = Integer.MAX_VALUE)
    @Config(category = ConfigCategory.ADVANCED3)
    public static int blinkAutoSendMs = -1;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean blinkCancelPacket = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean blinkShowCount = false;
}
