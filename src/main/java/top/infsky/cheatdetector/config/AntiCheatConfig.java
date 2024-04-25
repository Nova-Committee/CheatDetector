package top.infsky.cheatdetector.config;

import top.hendrixshen.magiclib.malilib.api.annotation.Config;
import top.hendrixshen.magiclib.malilib.api.annotation.Hotkey;
import top.hendrixshen.magiclib.malilib.api.annotation.Numeric;

public class AntiCheatConfig {
    @Hotkey
    @Config(category = ConfigCategory.ANTICHEAT)
    public static boolean antiCheatEnabled = true;

    @Hotkey
    @Config(category = ConfigCategory.ANTICHEAT)
    public static boolean disableSelfCheck = false;

    @Numeric(minValue = 0.0, maxValue = Double.MAX_VALUE)
    @Config(category = ConfigCategory.ANTICHEAT)
    public static double threshold = 1.0;

    @Numeric(minValue = -1, maxValue = Integer.MAX_VALUE)
    @Config(category = ConfigCategory.ANTICHEAT)
    public static int VLClearTime = 6000;
}
