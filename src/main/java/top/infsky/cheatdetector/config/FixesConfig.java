package top.infsky.cheatdetector.config;

import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.malilib.api.annotation.*;
import top.infsky.cheatdetector.config.utils.ConfigCategory;
import top.infsky.cheatdetector.config.utils.Fixes;

public class FixesConfig {
    @Config(category = ConfigCategory.FIXES)
    public static boolean packetFixEnabled = false;

    @Config(category = ConfigCategory.FIXES)
    public static @NotNull String packetFixMode = "NORMAL";

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
