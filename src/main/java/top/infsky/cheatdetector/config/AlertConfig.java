package top.infsky.cheatdetector.config;

import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.malilib.api.annotation.Config;
import top.infsky.cheatdetector.config.utils.ConfigCategory;

public class AlertConfig {
    @Config(category = ConfigCategory.ALERT)
    public static boolean allowAlert = true;

    @Config(category = ConfigCategory.ALERT)
    public static boolean allowAlertFixes = false;

    @Config(category = ConfigCategory.ALERT)
    public static boolean allowAlertVLClear = false;

    @Config(category = ConfigCategory.ALERT)
    public static boolean disableBuffer = false;

    @Config(category = ConfigCategory.ALERT)
    public static @NotNull String prefix = "§b§lTR§r§l>";
}
