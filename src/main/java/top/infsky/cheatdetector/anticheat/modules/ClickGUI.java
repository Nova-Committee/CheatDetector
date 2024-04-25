package top.infsky.cheatdetector.anticheat.modules;

import lombok.Getter;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.malilib.impl.ConfigManager;
import top.hendrixshen.magiclib.malilib.impl.gui.ConfigGui;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.anticheat.Module;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.anticheat.TRSelf;
import top.infsky.cheatdetector.config.*;

public class ClickGUI extends Module {
    @Getter(lazy = true)
    private static final @NotNull ConfigGui instance = create(CheatDetector.MOD_ID, ConfigCategory.ANTICHEAT, CheatDetector.CONFIG_HANDLER.configManager);

    public ClickGUI(@NotNull TRSelf player) {
        super("ClickGUI", player);
    }

    @Override
    public void _onTick() {
//        LogUtils.custom(ModuleConfig.clickGUIEnabled ? "yes" : "no");
        if (ModuleConfig.clickGUIEnabled) {
            ModuleConfig.clickGUIEnabled = false;
            TRPlayer.CLIENT.setScreen(getInstance());
            update();
        }
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
        getInstance().reDraw();
        CheatDetector.CONFIG_HANDLER.save();
    }
}