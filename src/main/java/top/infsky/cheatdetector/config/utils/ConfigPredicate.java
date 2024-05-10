package top.infsky.cheatdetector.config.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import top.hendrixshen.magiclib.dependency.api.ConfigDependencyPredicate;
import top.hendrixshen.magiclib.malilib.impl.ConfigManager;
import top.hendrixshen.magiclib.malilib.impl.ConfigOption;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;

import java.util.List;

public class ConfigPredicate {
    public static void onJoinWorld(String ip, ConfigManager configManager) {
        if (!PASMode.getIpAddresses().contains(ip)) {
            configManager.setValue("aaaPASModeEnabled", false);
            ModuleConfig.aaaPASModeEnabled = false;
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

    public static class FakelagLatencyMode implements ConfigDependencyPredicate {
        @Override
        public boolean isSatisfied(ConfigOption configOption) {
            return Advanced3Config.getFakelagMode() == Advanced3Config.FakelagMode.LATENCY;
        }
    }

    public static class FakelagDynamicMode implements ConfigDependencyPredicate {
        @Override
        public boolean isSatisfied(ConfigOption configOption) {
            return Advanced3Config.getFakelagMode() == Advanced3Config.FakelagMode.DYNAMIC;
        }
    }

    public static class AimAssistLegitMode implements ConfigDependencyPredicate {
        @Override
        public boolean isSatisfied(ConfigOption configOption) {
            return !Advanced3Config.aimAssistInteract;
        }
    }

    private abstract static class ServerMode implements ConfigDependencyPredicate {
    }
}
