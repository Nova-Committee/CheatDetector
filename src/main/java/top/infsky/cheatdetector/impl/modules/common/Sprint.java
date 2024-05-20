package top.infsky.cheatdetector.impl.modules.common;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.utils.TRSelf;

public class Sprint extends Module {
    @Getter
    @Nullable
    public static Module instance = null;

    public Sprint(@NotNull TRSelf player) {
        super("Sprint", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) return;
        TRPlayer.CLIENT.options.keySprint.setDown(true);
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.sprintEnabled;
    }
}
