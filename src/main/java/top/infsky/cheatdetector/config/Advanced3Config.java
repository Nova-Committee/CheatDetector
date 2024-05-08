package top.infsky.cheatdetector.config;

import top.hendrixshen.magiclib.malilib.api.annotation.Config;
import top.hendrixshen.magiclib.malilib.api.annotation.Numeric;
import top.infsky.cheatdetector.config.utils.ConfigPredicate;
import top.infsky.cheatdetector.impl.utils.notebot.NotebotUtils;
import top.infsky.cheatdetector.config.utils.ConfigCategory;

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
    public static boolean spinAutoPause = false;
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
    public static double airPlaceReach = 4.5;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean airPlaceNoSwing = false;

    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean invWalkAllowSneak = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean invWalkAllowSprint = true;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean invWalkAllowJump = true;

    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean backtrackShowCount = false;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static int backtrackDelayMs = 100;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean backtrackCancelPacket = false;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean backtrackRenderRealPosition = false;

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
    public static boolean scaffoldSilentKeepRotation = true;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean scaffoldNoSprint = false;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static int scaffoldExpend = 0;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean scaffoldAutoSwitch = false;
    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static int scaffoldPlaceMinDelay = 1;

    @Config(category = ConfigCategory.ADVANCED3, predicate = ConfigPredicate.PASMode.class)
    public static boolean velocityOnlyHurt = true;

    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean killauraAttack = true;
    @Config(category = ConfigCategory.ADVANCED3)
    public static double killauraAttackReach = 3.0;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean killauraPreAim = true;
    @Config(category = ConfigCategory.ADVANCED3)
    public static double killauraPreAimReach = 6;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean killauraSwitch = true;
    @Config(category = ConfigCategory.ADVANCED3)
    public static int killauraSwitchDelay = 1;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean killauraRayCast = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean killauraLookView = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean killauraNoRotation = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static double killauraMinCPS = 2;
    @Config(category = ConfigCategory.ADVANCED3)
    public static double killauraMaxCPS = 2;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean killauraLegitAim = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean killauraLegitAimNoise = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean killauraIncludeArmorStands = true;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean killauraIncludeEntities = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean killauraIncludePlayers = false;

    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean handSpinPerfectSwing = false;
    @Config(category = ConfigCategory.ADVANCED3)
    public static int handSpinSwingDelay = 0;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean handSpinMainHand = true;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean handSpinOffHand = true;
    @Config(category = ConfigCategory.ADVANCED3)
    public static boolean handSpinDiffSwing = false;

    public static NotebotUtils.NotebotMode getNoteBotMode() {
        if (noteBotMode.equals("AnyInstrument")) {
            return NotebotUtils.NotebotMode.AnyInstrument;
        }
        return NotebotUtils.NotebotMode.ExactInstruments;
    }
}
