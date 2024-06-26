package top.infsky.cheatdetector.config.utils;

import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import top.hendrixshen.magiclib.dependency.api.ConfigDependencyPredicate;
import top.hendrixshen.magiclib.malilib.impl.ConfigManager;
import top.hendrixshen.magiclib.malilib.impl.ConfigOption;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.AntiCheatConfig;
import top.infsky.cheatdetector.config.DangerConfig;
import top.infsky.cheatdetector.config.ModuleConfig;

import java.util.List;

public class ConfigPredicate {
    public static void onJoinWorld(String ip, @NotNull ConfigManager configManager) {
        configManager.setValue("aaaPASModeEnabled", PASMode.getIpAddresses().contains(ip));
    }

    public static class MiniHudLoaded implements ConfigDependencyPredicate {
        @Override
        public boolean isSatisfied(ConfigOption option) {
            return FabricLoader.getInstance().isModLoaded("minihud");
        }

    }

    public static class ExperimentalMode implements ConfigDependencyPredicate {
        @Override
        public boolean isSatisfied(ConfigOption option) {
            return AntiCheatConfig.experimentalCheck;
        }

    }

    public static class PASMode implements ConfigDependencyPredicate {
        public static @NotNull @Unmodifiable List<String> getIpAddresses() {
            return List.of("touch.dls3.top", "play.sry25565.online");
        }
        @Override
        public boolean isSatisfied(ConfigOption option) {
            return ModuleConfig.aaaPASModeEnabled;
        }

    }

    public static class DangerMode implements ConfigDependencyPredicate {
        @Override
        public boolean isSatisfied(ConfigOption option) {
            return DangerConfig.aaaDangerModeEnabled;
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
}
