package top.infsky.cheatdetector.config;

import top.hendrixshen.magiclib.malilib.api.annotation.*;
import top.infsky.cheatdetector.config.utils.ConfigCategory;
import top.infsky.cheatdetector.config.utils.ConfigPredicate;

public class AdvancedConfig {
    @Config(category = ConfigCategory.ADVANCED)
    public static boolean blinkCheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int blinkAlertBuffer = 10;
    @Config(category = ConfigCategory.ADVANCED)
    public static double blinkMaxDistance = 8;

    @Config(category = ConfigCategory.ADVANCED)
    public static boolean gameModeACheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int gameModeAAlertBuffer = 1;


    @Config(category = ConfigCategory.ADVANCED)
    public static boolean highJumpACheck = false;
    @Config(category = ConfigCategory.ADVANCED)
    public static int highJumpAAlertBuffer = 5;
    @Config(category = ConfigCategory.ADVANCED)
    public static boolean highJumpAFlagOne = true;


    @Config(category = ConfigCategory.ADVANCED)
    public static boolean noSlowACheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int noSlowAAlertBuffer = 20;
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
    public static boolean motionACheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int motionAAlertBuffer = 10;

    @Config(category = ConfigCategory.ADVANCED)
    public static boolean flyBCheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int flyBAlertBuffer = 10;

    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static boolean reachACheck = true;
    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static int reachAAlertBuffer = 4;
    @Numeric(minValue = 0, maxValue = 6)
    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static double reachADefaultReach = 3.5;
    @Numeric(minValue = 0, maxValue = 4)
    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static int reachACheckDelay = 2;

    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static boolean hitBoxACheck = true;
    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static int hitBoxAAlertBuffer = 4;
    @Numeric(minValue = 0, maxValue = 4)
    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static int hitBoxACheckDelay = 2;

    @Config(category = ConfigCategory.ADVANCED)
    public static boolean flyACheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int flyAAlertBuffer = 30;

    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static boolean autoClickerACheck = true;
    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static int autoClickerAAlertBuffer = 10;
    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static int autoClickerAMinDiffMs = 5;

    @Config(category = ConfigCategory.ADVANCED)
    public static boolean flyCCheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int flyCAlertBuffer = 30;
    @Config(category = ConfigCategory.ADVANCED)
    public static int flyCMinRepeatTicks = 10;

    @Config(category = ConfigCategory.ADVANCED)
    public static boolean boatFlyACheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int boatFlyAAlertBuffer = 20;

    @Config(category = ConfigCategory.ADVANCED)
    public static boolean strafeACheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int strafeAAlertBuffer = 20;
    @Config(category = ConfigCategory.ADVANCED)
    public static double strafeAMaxDiffToFlag = 0.005;

    @Config(category = ConfigCategory.ADVANCED)
    public static boolean timerACheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int timerAAlertBuffer = 20;
    @Config(category = ConfigCategory.ADVANCED)
    public static int timerADefaultBalance = -300;
    @Config(category = ConfigCategory.ADVANCED)
    public static int timerAFlagBalance = 100;

    @Config(category = ConfigCategory.ADVANCED)
    public static boolean invalidPitchCheck = true;
    @Config(category = ConfigCategory.ADVANCED)
    public static int invalidPitchAlertBuffer = 1;

    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static boolean aimACheck = true;
    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static int aimAAlertBuffer = 30;
    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static boolean aimAOnlyOnSwing = false;
    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static boolean aimAOnlyIfTargetIsMoving = true;
    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static boolean aimAOnlyPlayer = false;
    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static double aimAMinDiffYaw = 2;
    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static double aimAMinDeltaYaw = 2;
    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static double aimAMinDiffPitch = 2;
    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static double aimAMinDeltaPitch = 2;
    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static double aimAMaxDistance = 6;

    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static boolean scaffoldACheck = false;
    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static int scaffoldAAlertBuffer = 30;

    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static boolean aimBCheck = true;
    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static int aimBAlertBuffer = 10;
    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static double aimBMinDiffYaw = 2;
    @Config(category = ConfigCategory.ADVANCED, predicate = ConfigPredicate.ExperimentalMode.class)
    public static double aimBMinDiffPitch = 2;

    public static short getNoSlowAInJumpDisableTick() {
        return (short) noSlowAInJumpDisableTick;
    }

    public static short getSpeedAAfterJumpJumpTick() {
        return (short) speedAAfterJumpJumpTick;
    }

}
