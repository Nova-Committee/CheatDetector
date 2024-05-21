package top.infsky.cheatdetector.impl.modules;

import lombok.Getter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.malilib.impl.ConfigManager;
import top.hendrixshen.magiclib.malilib.impl.gui.ConfigGui;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.config.*;
import top.infsky.cheatdetector.config.utils.ConfigCategory;

public class ClickGUI extends Module {
    @Getter
    @Nullable
    private static Module instance = null;

    @Getter(lazy = true)
    private static final @NotNull ConfigGui configGui = create(CheatDetector.MOD_ID, ConfigCategory.ANTICHEAT, CheatDetector.CONFIG_HANDLER.configManager);

    public ClickGUI(@NotNull TRSelf player) {
        super("ClickGUI", player);
        instance = this;
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.clickGUIEnabled;
    }

    @Override
    public void _onTick() {
//        LogUtils.custom(ModuleConfig.clickGUIEnabled ? "yes" : "no");
        if (!isDisabled()) {
            CheatDetector.CONFIG_HANDLER.configManager.setValue("clickGUIEnabled", false);
            getConfigGui().setParent(null);
            TRPlayer.CLIENT.setScreen(getConfigGui());
            update();
        }
    }

    @Contract("_, _, _ -> new")
    public static @NotNull ConfigGui create(String identifier, String defaultTab, ConfigManager configManager) {
        return new ConfigGui(identifier, defaultTab, configManager,
                Component.translatable("cheatdetector.pretty_name")
                        .append(" ")
                        .append(
                                FabricLoader.getInstance().getModContainer(CheatDetector.MOD_ID).orElseThrow().getMetadata().getVersion().getFriendlyString()
                        )
                        .getString()
        );
    }

    public static void register(@NotNull ConfigManager manager) {
        manager.parseConfigClass(AntiCheatConfig.class);
        manager.parseConfigClass(FixesConfig.class);
        manager.parseConfigClass(ModuleConfig.class);
        manager.parseConfigClass(DangerConfig.class);
        manager.parseConfigClass(AlertConfig.class);
        manager.parseConfigClass(AdvancedConfig.class);
        manager.parseConfigClass(Advanced2Config.class);
        manager.parseConfigClass(Advanced3Config.class);
    }

    public static void update() {
        getConfigGui().reDraw();
        CheatDetector.CONFIG_HANDLER.save();
    }
}
