package top.infsky.cheatdetector.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.malilib.impl.gui.ConfigGui;
import top.infsky.cheatdetector.anticheat.modules.ClickGUI;

public class ModMenu implements ModMenuApi {
    @Override
    public @NotNull ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> {
            ConfigGui gui = ClickGUI.getInstance();
            gui.setParent(screen);
            return gui;
        };
    }
}
