package top.infsky.cheatdetector.impl.modules;

import lombok.Getter;
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

    private boolean lastPASModeEnabled = false;
    private Advanced3Config.FakelagMode lastFakelagMode = Advanced3Config.FakelagMode.LATENCY;
    private boolean lastAimAssistInteract = false;

    public ClickGUI(@NotNull TRSelf player) {
        super("ClickGUI", player);
        instance = this;
    }

    @Override
    public void _onTick() {
//        LogUtils.custom(ModuleConfig.clickGUIEnabled ? "yes" : "no");
        if (ModuleConfig.clickGUIEnabled) {
            CheatDetector.CONFIG_HANDLER.configManager.setValue("clickGUIEnabled", false);
            getConfigGui().setParent(null);
            TRPlayer.CLIENT.setScreen(getConfigGui());
            update();
        }

        if (lastPASModeEnabled != ModuleConfig.aaaPASModeEnabled
                || lastFakelagMode != Advanced3Config.getFakelagMode()
                || lastAimAssistInteract != Advanced3Config.aimAssistInteract) update();
        lastPASModeEnabled = ModuleConfig.aaaPASModeEnabled;
        lastFakelagMode = Advanced3Config.getFakelagMode();
        lastAimAssistInteract = Advanced3Config.aimAssistInteract;
    }

    @Contract("_, _, _ -> new")
    public static @NotNull ConfigGui create(String identifier, String defaultTab, ConfigManager configManager) {
        return new ConfigGui(identifier, defaultTab, configManager, Component.translatable("cheatdetector.gui.title").getString());
    }

    public static void register(@NotNull ConfigManager manager) {
        manager.parseConfigClass(AntiCheatConfig.class);
        manager.parseConfigClass(FixesConfig.class);
        manager.parseConfigClass(ModuleConfig.class);
        manager.parseConfigClass(AlertConfig.class);
        manager.parseConfigClass(AdvancedConfig.class);
        manager.parseConfigClass(Advanced2Config.class);
        manager.parseConfigClass(Advanced3Config.class);
    }

    /*
    如果你修改了配置文件，执行这个函数以显式更新。
    TODO 自动更新仍在开发中。
     */
    public static void update() {
        getConfigGui().reDraw();
//        CheatDetector.CONFIG_HANDLER.save();
    }
}
