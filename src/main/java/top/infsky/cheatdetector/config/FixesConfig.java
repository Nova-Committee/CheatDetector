package top.infsky.cheatdetector.config;

import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.malilib.api.annotation.*;

public class FixesConfig {
    @Hotkey
    @Config(category = ConfigCategory.FIXES)
    public static boolean packetFixEnabled = false;

    @Config(category = ConfigCategory.FIXES)
    public static @NotNull String packetFixMode = "NORMAL";

    @Hotkey
    @Config(category = ConfigCategory.FIXES)
    public static boolean vulcanDisablerEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.FIXES)
    public static boolean vulcanOmniSprintEnabled = false;

    public static @NotNull Fixes getPacketFixMode() {
        Fixes result;
        switch (packetFixMode.toUpperCase()) {
            case "NORMAL" -> result = Fixes.NORMAL;
            case "STRICT" -> result = Fixes.STRICT;
            default -> {
                packetFixMode = "NORMAL";
                result = Fixes.NORMAL;
            }
        }
        return result;
    }
}
