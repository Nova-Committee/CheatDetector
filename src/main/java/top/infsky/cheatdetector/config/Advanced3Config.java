package top.infsky.cheatdetector.config;

import top.hendrixshen.magiclib.malilib.api.annotation.Config;
import top.hendrixshen.magiclib.malilib.api.annotation.Numeric;
import top.infsky.cheatdetector.config.utils.ConfigPredicate;
import top.infsky.cheatdetector.impl.utils.notebot.NotebotUtils;
import top.infsky.cheatdetector.config.utils.ConfigCategory;

public class Advanced3Config {
    @Numeric(minValue = 0, maxValue = Integer.MAX_VALUE)
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
    @Numeric(minValue = 0, maxValue = 360)
    @Config(category = ConfigCategory.ADVANCED3)
    public static double spinYawStep = 45;
    @Numeric(minValue = 0, maxValue = 180)
    @Config(category = ConfigCategory.ADVANCED3)
    public static double spinPitchStep = 35;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean spinOnlyPacket = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean spinAutoPause = false;
    @Numeric(minValue = 0, maxValue = Integer.MAX_VALUE)
    @Config(category = ConfigCategory.ADVANCED3)
    public static int spinAutoPauseTime = 10;

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

    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean antiFallAutoDisabled = false;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static int antiFallFallDistance = 10;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean antiFallFastClutchOnVoid = false;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean antiFallClutchMsg = false;

    @Config(category = ConfigCategory.ADVANCED3)
    public static String fakelagMode = "LATENCY";
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.FakelagLatencyMode.class)
    public static boolean fakelagIncludeOutgoing = true;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.FakelagLatencyMode.class)
    public static boolean fakelagIncludeIncoming = false;
    @Numeric(minValue = 0, maxValue = Integer.MAX_VALUE)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.FakelagLatencyMode.class)
    public static int fakelagDelayMs = 100;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.FakelagLatencyMode.class)
    public static boolean fakelagShowCount = false;
    @Numeric(minValue = 0, maxValue = Integer.MAX_VALUE)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.FakelagDynamicMode.class)
    public static double fakelagMaxTargetRange = 15.0;
    @Numeric(minValue = 0, maxValue = Integer.MAX_VALUE)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.FakelagDynamicMode.class)
    public static double fakelagStartRange = 6.0;
    @Numeric(minValue = 0, maxValue = Integer.MAX_VALUE)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.FakelagDynamicMode.class)
    public static double fakelagStopRange = 1.0;
    @Numeric(minValue = 0, maxValue = Integer.MAX_VALUE)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.FakelagDynamicMode.class)
    public static int fakelagMaxLagTicks = 100;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.FakelagDynamicMode.class)
    public static boolean fakelagOnlyOutgoing = true;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.FakelagDynamicMode.class)
    public static boolean fakelagStopOnHurt = true;
    @Numeric(minValue = 0, maxValue = Integer.MAX_VALUE)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.FakelagDynamicMode.class)
    public static int fakelagPauseTicksOnHurt = 10;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.FakelagDynamicMode.class)
    public static boolean fakelagDebug = false;

    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean fakelagAutoDisable = false;


    @Numeric(minValue = 0, maxValue = 8)
    @Config(category = ConfigCategory.ADVANCED3)
    public static double airPlaceReach = 4.5;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean airPlaceNoSwing = false;

    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean invWalkAllowSneak = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean invWalkAllowSprint = true;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean invWalkAllowJump = true;

    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean noteBotRoundOutOfRange = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static String noteBotMode = "ExactInstruments";
    @Config(category = ConfigCategory.ADVANCED3)
    public static String noteBotFilePath = "";
    @Numeric(minValue = 1, maxValue = 20)
    @Config(category = ConfigCategory.ADVANCED3)
    public static int noteBotCheckNoteblocksAgainDelay = 10;
    @Numeric(minValue = 1, maxValue = 20)
    @Config(category = ConfigCategory.ADVANCED3)
    public static int noteBotTickDelay = 1;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean noteBotSwingArm = true;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean noteBotAutoRotate = true;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean noteBotSilentRotate = true;
    @Numeric(minValue = 1, maxValue = 20)
    @Config(category = ConfigCategory.ADVANCED3)
    public static int noteBotConcurrentTuneBlocks = 1;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean noteBotPolyphonic = true;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean noteBotDebug = false;

    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean scaffoldNoSwing = false;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean scaffoldDoRotation = true;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean scaffoldSilentRotation = true;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean scaffoldNoSprint = false;
    @Numeric(minValue = 0, maxValue = 6, useSlider = true)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static int scaffoldExpend = 0;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean scaffoldAutoSwitch = false;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean scaffoldSameY = false;
    @Numeric(minValue = 0, maxValue = Integer.MAX_VALUE)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static int scaffoldPlaceMinDelay = 1;

    @Numeric(minValue = 0, maxValue = 15, useSlider = true)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static double aimAssistRange = 6;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static boolean aimAssistInteract = false;
    @Numeric(minValue = -1, maxValue = 1, useSlider = true)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static double aimAssistOffsetX = 0.024;
    @Numeric(minValue = -1, maxValue = 1, useSlider = true)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static double aimAssistOffsetY = -0.11;
    @Numeric(minValue = -1, maxValue = 1, useSlider = true)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static double aimAssistOffsetZ = -0.018;
    @Numeric(minValue = 0, maxValue = 50)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static double aimAssistYawSpeed = 4.5;
    @Numeric(minValue = 0, maxValue = 50)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static double aimAssistPitchSpeed = 2.0;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static boolean aimAssistStopOnTarget = false;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static boolean aimAssistNoise1 = false;
    @Numeric(minValue = 0, maxValue = 5, useSlider = true)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static double aimAssistYawRandom = 1.3;
    @Numeric(minValue = 0, maxValue = 5, useSlider = true)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static double aimAssistPitchRandom = 1.08;
    @Numeric(minValue = 0, maxValue = 1, useSlider = true)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static double aimAssistXZRandom = 0.08;
    @Numeric(minValue = 0, maxValue = 1, useSlider = true)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static double aimAssistYRandom = 0.1;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static boolean aimAssistNoise2 = false;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static boolean aimAssistIncludeArmorStands = false;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static boolean aimAssistIncludeEntities = true;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static boolean aimAssistIncludeAnyObjects = false;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static boolean aimAssistIncludePlayers = false;

    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean handSpinPerfectSwing = false;
    @Numeric(minValue = 0, maxValue = Integer.MAX_VALUE)
    @Config(category = ConfigCategory.ADVANCED3)
    public static int handSpinSwingDelay = 0;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean handSpinMainHand = true;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean handSpinOffHand = true;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean handSpinDiffSwing = false;

    @Numeric(minValue = 0, maxValue = 6)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static double nukerRange = 4.5;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static boolean nukerDoRotation = true;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static boolean nukerSilentRotation = true;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static boolean nukerKeepGround = false;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static boolean nukerYCheck = false;
    @Numeric(minValue = 1, maxValue = 20)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static int nukerMultiBreak = 1;

    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static boolean airStuckCancelPacket = true;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static boolean airStuckLegit = false;
    @Numeric(minValue = -1, maxValue = 20)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static double airStuckMinDistanceBeforeGround = -1;
    @Numeric(minValue = -1, maxValue = 20)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static double airStuckMinFallDistance = -1;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static boolean airStuckAntiKick = false;

    @Config(category = ConfigCategory.ADVANCED3)
    public static String clientSpoofBrand = "vanilla";

    @Numeric(minValue = 0, maxValue = 5)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static double flyAcceptTeleportMaxDistance = 1;

    @Numeric(minValue = 1, maxValue = Integer.MAX_VALUE)
    @Config(category = ConfigCategory.ADVANCED3)
    public static int writerPart = 1;

    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean writerToBook = false;

    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean antiBotMessage = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean antiBotLatency = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean antiBotHypixel = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean antiBotDebug = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean antiBotApi = false;

    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean rotationFixSprintVulcan = true;
    @Config(category = ConfigCategory.ADVANCED3)
    public static int rotationFixSprintDelay = 10;

    @Numeric(minValue = -10, maxValue = 10)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static double slowMotionMultiplyXZ = 0.6;
    @Numeric(minValue = -10, maxValue = 10)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static double slowMotionMultiplyY = 0.6;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static boolean slowMotionLimit = false;
    @Numeric(minValue = 0, maxValue = 10)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static double slowMotionLimitXZ = 0.15;
    @Numeric(minValue = 0, maxValue = 10)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static double slowMotionLimitY = 0.15;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static boolean slowMotionOnlyElytra = false;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static boolean slowMotionNotWhileFallFlying = false;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static boolean slowMotionStopOnDisabled = false;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.DangerMode.class)
    public static boolean slowMotionFastStop = false;

    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static String autoCatchName = "";
    @Numeric(minValue = 0, maxValue = 6)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static double autoCatchDistance = 6;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean autoCatchFast = false;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean autoCatchAlways = false;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean autoCatchExtraPost = false;
    @Numeric(minValue = 1, maxValue = 49)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static int autoCatchExtraPostDelayMs = 25;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean autoCatchAsPossible = false;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean autoCatchResetMotion = false;
    @Numeric(minValue = 0, maxValue = 6)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static double autoCatchAsPossibleTeleportDistance = 6;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean autoCatchRotate = false;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean autoCatchSilentRotate = false;

    @Numeric(minValue = 0, maxValue = 4)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static int speedBoost = 2;

    @Numeric(minValue = 1.0, maxValue = 4.0)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static double velocityHorizon = 2.0;
    @Numeric(minValue = 1.0, maxValue = 3.0)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static double velocityVertical = 1.0;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean velocityReserve = false;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean velocityStrafe = false;
    @Numeric(minValue = 1, maxValue = 10)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static int velocityStrafeMinHurtTime = 7;

    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean noStopBreakSilent = false;

    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static String surroundName = "";
    @Numeric(minValue = 0, maxValue = 8)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static double surroundDistance = 8;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean surroundAutoSwitch = true;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean surroundAutoSwitchBack = true;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean surroundDoRotation = false;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean surroundSilentRotation = true;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean surroundSwing = false;
    @Numeric(minValue = 0, maxValue = 20)
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static int surroundPlaceDelay = 0;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean surroundSmartPlaceDelay = true;

    @Config(category = ConfigCategory.ADVANCED3)
    public static double debugDouble = 0.0;

    public static NotebotUtils.NotebotMode getNoteBotMode() {
        if (noteBotMode.equals("AnyInstrument")) {
            return NotebotUtils.NotebotMode.AnyInstrument;
        }
        return NotebotUtils.NotebotMode.ExactInstruments;
    }

    public static FakelagMode getFakelagMode() {
        if (fakelagMode.equalsIgnoreCase("DYNAMIC")) {
            return FakelagMode.DYNAMIC;
        }
        return FakelagMode.LATENCY;
    }

    public enum FakelagMode {
        LATENCY,
        DYNAMIC
    }
}
