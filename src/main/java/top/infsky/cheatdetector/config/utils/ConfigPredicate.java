package top.infsky.cheatdetector.config.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import top.hendrixshen.magiclib.dependency.api.ConfigDependencyPredicate;
import top.hendrixshen.magiclib.malilib.impl.ConfigManager;
import top.hendrixshen.magiclib.malilib.impl.ConfigOption;
import top.infsky.cheatdetector.config.ModuleConfig;

import java.util.List;

public class ConfigPredicate {
    public static void onJoinWorld(String ip, ConfigManager configManager) {
        if (!PASMode.getIpAddresses().contains(ip)) {
            configManager.setValue("aaaPASModeEnabled", false);
            ModuleConfig.aaaPASModeEnabled = false;
        }
        if (!HypixelMode.getIpAddresses().contains(ip)) {
            configManager.setValue("aaaHypixelModeEnabled", false);
            ModuleConfig.aaaHypixelModeEnabled = false;
        }
    }


    public static class PASMode extends ServerMode {
        public static @NotNull @Unmodifiable List<String> getIpAddresses() {
            return List.of("touch.dls3.top", "play.sry25565.online");
        }
        @Override
        public boolean isSatisfied(ConfigOption option) {
            return ModuleConfig.aaaPASModeEnabled;
        }

    }

    public static class HypixelMode extends ServerMode {
        public static @NotNull @Unmodifiable List<String> getIpAddresses() {
            return List.of("mc.hypixel.net");
        }
        @Override
        public boolean isSatisfied(ConfigOption option) {
            return ModuleConfig.aaaHypixelModeEnabled;
        }
    }

    private abstract static class ServerMode implements ConfigDependencyPredicate {
        @Contract(pure = true)
        public static @NotNull @Unmodifiable List<String> getIpAddresses() {
            return List.of();
        }
    }
}
