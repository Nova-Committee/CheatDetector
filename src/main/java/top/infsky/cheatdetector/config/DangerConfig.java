package top.infsky.cheatdetector.config;

import top.hendrixshen.magiclib.malilib.api.annotation.Config;
import top.hendrixshen.magiclib.malilib.api.annotation.Hotkey;
import top.infsky.cheatdetector.config.utils.ConfigCategory;
import top.infsky.cheatdetector.config.utils.ConfigPredicate;

public class DangerConfig {
    @Config(category = ConfigCategory.DANGER)
    public static boolean aaaDangerModeEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.DANGER, predicate = ConfigPredicate.DangerMode.class)
    public static boolean flyEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.DANGER, predicate = ConfigPredicate.DangerMode.class)
    public static boolean airStuckEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.DANGER, predicate = ConfigPredicate.DangerMode.class)
    public static boolean slowMotionEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.DANGER, predicate = ConfigPredicate.DangerMode.class)
    public static boolean nukerEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.DANGER, predicate = ConfigPredicate.DangerMode.class)
    public static boolean aimAssistEnabled = false;
}
