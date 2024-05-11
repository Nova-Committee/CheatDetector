package top.infsky.cheatdetector.config;

import top.hendrixshen.magiclib.malilib.api.annotation.*;
import top.infsky.cheatdetector.config.utils.ConfigCategory;

public class AdvancedConfig {
    @Config(category = ConfigCategory.ADVANCED)
    public static boolean blinkCheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int blinkAlertBuffer = 10;
    @Config(category = ConfigCategory.ADVANCED)
    public static double blinkMaxDistance = 8;


    @Config(category = ConfigCategory.ADVANCED)
    public static boolean flyACheck = false;
    @Config(category = ConfigCategory.ADVANCED)
    public static int flyAlertBuffer = 20;
    @Config(category = ConfigCategory.ADVANCED)
    public static int flyAOnGroundJumpTick = 1;
    @Config(category = ConfigCategory.ADVANCED)
    public static int flyAInLiquidLiquidTick = 8;
    @Config(category = ConfigCategory.ADVANCED)
    public static int flyAInHurtJumpTick = 6;
    @Config(category = ConfigCategory.ADVANCED)
    public static double flyAFromWaterYDistance = 0.5;
    @Config(category = ConfigCategory.ADVANCED)
    public static int flyAOnTeleportDisableTick = 2;
    @Config(category = ConfigCategory.ADVANCED)
    public static int flyAOnJumpJumpTick = 24;


    @Config(category = ConfigCategory.ADVANCED)
    public static boolean flyBCheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int flyBAlertBuffer = 1;


    @Config(category = ConfigCategory.ADVANCED)
    public static boolean gameModeACheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int gameModeAAlertBuffer = 1;


    @Config(category = ConfigCategory.ADVANCED)
    public static boolean highJumpACheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int highJumpAAlertBuffer = 5;
    @Config(category = ConfigCategory.ADVANCED)
    public static boolean highJumpAFlagOne = true;


    @Config(category = ConfigCategory.ADVANCED)
    public static boolean noSlowACheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int noSlowAAlertBuffer = 20;
    @Config(category = ConfigCategory.ADVANCED)
    public static double noSlowASpeedTick1 = 2.56;
    @Config(category = ConfigCategory.ADVANCED)
    public static double noSlowASpeedTick2 = 1.92;
    @Config(category = ConfigCategory.ADVANCED)
    public static double noSlowASpeedTick3 = 1.6;
    @Config(category = ConfigCategory.ADVANCED)
    public static double noSlowASpeedTick4 = 1.4;
    @Config(category = ConfigCategory.ADVANCED)
    public static double noSlowASpeedTick5 = 1.36;
    @Config(category = ConfigCategory.ADVANCED)
    public static double noSlowASpeedTick6 = 1.26;
    @Config(category = ConfigCategory.ADVANCED)
    public static double noSlowASpeedTick7 = 1.18;
    @Config(category = ConfigCategory.ADVANCED)
    public static double noSlowASpeedTick8 = 1.16;
    @Config(category = ConfigCategory.ADVANCED)
    public static int noSlowAInJumpDisableTick = 4;


    @Config(category = ConfigCategory.ADVANCED)
    public static boolean speedACheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int speedAAlertBuffer = 30;
    @Config(category = ConfigCategory.ADVANCED)
    public static int speedAAfterJumpJumpTick = 10;
    @Config(category = ConfigCategory.ADVANCED)
    public static double speedAAfterJumpSpeed = 7.4;
    @Config(category = ConfigCategory.ADVANCED)
    public static double speedASprintingSpeed = 5.612;
    @Config(category = ConfigCategory.ADVANCED)
    public static double speedASilentSpeed = 1.295;
    @Config(category = ConfigCategory.ADVANCED)
    public static double speedAWalkSpeed = 4.317;


    @Config(category = ConfigCategory.ADVANCED)
    public static boolean speedBCheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int speedBAlertBuffer = 10;


    @Config(category = ConfigCategory.ADVANCED)
    public static boolean velocityACheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int velocityAAlertBuffer = 2;
    @Config(category = ConfigCategory.ADVANCED)
    public static int velocityAExtraDelayedMs = 20;
    @Config(category = ConfigCategory.ADVANCED)
    public static int velocityAAfterFallExtraDisableTick = 2;

    @Config(category = ConfigCategory.ADVANCED)
    public static boolean groundSpoofACheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int groundSpoofAAlertBuffer = 6;
    @Config(category = ConfigCategory.ADVANCED)
    public static boolean groundSpoofBCheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int groundSpoofBAlertBuffer = 6;

    @Config(category = ConfigCategory.ADVANCED)
    public static boolean autoBlockACheck = false;
    @Config(category = ConfigCategory.ADVANCED)
    public static int autoBlockAAlertBuffer = 4;

    @Config(category = ConfigCategory.ADVANCED)
    public static boolean speedCCheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int speedCAlertBuffer = 10;

    @Config(category = ConfigCategory.ADVANCED)
    public static boolean motionACheck = false;
    @Config(category = ConfigCategory.ADVANCED)
    public static int motionAAlertBuffer = 10;

    public static short getFlyAOnGroundJumpTick() {
        return (short) flyAOnGroundJumpTick;
    }

    public static short getFlyAInLiquidLiquidTick() {
        return (short) flyAInLiquidLiquidTick;
    }

    public static short getFlyAInHurtJumpTick() {
        return (short) flyAInHurtJumpTick;
    }

    public static short getFlyAOnTeleportDisableTick() {
        return (short) flyAOnTeleportDisableTick;
    }

    public static short getFlyAOnJumpJumpTick() {
        return (short) flyAOnJumpJumpTick;
    }

    public static short getNoSlowAInJumpDisableTick() {
        return (short) noSlowAInJumpDisableTick;
    }

    public static short getSpeedAAfterJumpJumpTick() {
        return (short) speedAAfterJumpJumpTick;
    }

}
