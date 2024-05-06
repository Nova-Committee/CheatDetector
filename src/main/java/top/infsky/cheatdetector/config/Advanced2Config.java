package top.infsky.cheatdetector.config;

import top.hendrixshen.magiclib.malilib.api.annotation.Config;
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
    public static boolean omniSprintShowPacketSend = false;
}
