package top.infsky.cheatdetector.config;

import top.hendrixshen.magiclib.malilib.api.annotation.*;

public class AdvancedConfig {
    @Config(category = ConfigCategory.ADVANCED)
    public static boolean blinkCheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int blinkAlertBuffer = 20;
    @Config(category = ConfigCategory.ADVANCED)
    public static double blinkMaxDistance = 1;


    @Config(category = ConfigCategory.ADVANCED)
    public static boolean flightACheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int flightAAlertBuffer = 20;
    @Config(category = ConfigCategory.ADVANCED)
    public static int flightAOnGroundJumpTick = 1;
    @Config(category = ConfigCategory.ADVANCED)
    public static int flightAInLiquidLiquidTick = 8;
    @Config(category = ConfigCategory.ADVANCED)
    public static int flightAInHurtJumpTick = 6;
    @Config(category = ConfigCategory.ADVANCED)
    public static double flightAJumpDistance = 1.25219;
    @Config(category = ConfigCategory.ADVANCED)
    public static double flightAFromWaterYDistance = 0.5;
    @Config(category = ConfigCategory.ADVANCED)
    public static int flightAOnTeleportDisableTick = 2;
    @Config(category = ConfigCategory.ADVANCED)
    public static int flightAOnJumpJumpTick = 14;


    @Config(category = ConfigCategory.ADVANCED)
    public static boolean flightBCheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int flightBAlertBuffer = 1;


    @Config(category = ConfigCategory.ADVANCED)
    public static boolean gameModeACheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int gameModeAAlertBuffer = 1;


    @Config(category = ConfigCategory.ADVANCED)
    public static boolean highJumpACheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int highJumpAAlertBuffer = 10;
    @Config(category = ConfigCategory.ADVANCED)
    public static double highJumpAJumpDistance = 1.25219;
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

    public static short getFlightAOnGroundJumpTick() {
        return (short) flightAOnGroundJumpTick;
    }

    public static short getFlightAInLiquidLiquidTick() {
        return (short) flightAInLiquidLiquidTick;
    }

    public static short getFlightAInHurtJumpTick() {
        return (short) flightAInHurtJumpTick;
    }

    public static short getFlightAOnTeleportDisableTick() {
        return (short) flightAOnTeleportDisableTick;
    }

    public static short getFlightAOnJumpJumpTick() {
        return (short) flightAOnJumpJumpTick;
    }

    public static short getNoSlowAInJumpDisableTick() {
        return (short) noSlowAInJumpDisableTick;
    }

    public static short getSpeedAAfterJumpJumpTick() {
        return (short) speedAAfterJumpJumpTick;
    }

}
