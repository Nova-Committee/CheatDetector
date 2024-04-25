package top.infsky.cheatdetector.config;

import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.malilib.api.annotation.Config;
import top.hendrixshen.magiclib.malilib.api.annotation.Hotkey;

public class AlertConfig {
    @Hotkey
    @Config(category = ConfigCategory.ALERT)
    public static boolean allowAlert = true;

    @Hotkey
    @Config(category = ConfigCategory.ALERT)
    public static boolean allowAlertFixes = false;

    @Hotkey
    @Config(category = ConfigCategory.ALERT)
    public static boolean allowAlertVLClear = false;

    @Hotkey
    @Config(category = ConfigCategory.ALERT)
    public static boolean disableBuffer = false;

    @Config(category = ConfigCategory.ALERT)
    public static @NotNull String prefix = "§b§lTR§r§l>";
}
