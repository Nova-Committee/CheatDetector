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
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean blinkIncludeInComing = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean blinkAutoDisable = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean blinkCancelPacket = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean blinkShowCount = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean blinkRenderRealPosition = false;

    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean airWalkSameY = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean airWalkAutoJump = true;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean airWalkAntiFlag = true;

    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean antiFallAutoDisabled = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static int antiFallFallDistance = 10;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean antiFallOnlyOnVoid = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean antiFallClutchMsg = false;

    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean fakelagIncludeOutgoing = true;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean fakelagIncludeIncoming = false;
    @Numeric(minValue = 0, maxValue = Integer.MAX_VALUE)
    @Config(category = ConfigCategory.ADVANCED3)
    public static int fakelagDelayMs = 100;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean fakelagAutoDisable = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean fakelagShowCount = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean fakelagRenderRealPosition = false;

    @Config(category = ConfigCategory.ADVANCED3)
    public static double airPlaceReach = 4.5;

    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean invWalkAllowSneak = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean invWalkAllowSprint = true;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean invWalkAllowJump = true;

    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean backtrackShowCount = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static int backtrackDelayMs = 100;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean backtrackCancelPacket = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean backtrackRenderRealPosition = false;
}
