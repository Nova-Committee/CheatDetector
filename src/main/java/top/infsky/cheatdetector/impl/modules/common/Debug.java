package top.infsky.cheatdetector.impl.modules.common;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.impl.utils.world.PlayerMove;
import top.infsky.cheatdetector.utils.TRSelf;

public class Debug extends Module {
    @Getter
    @Nullable
    private static Module instance = null;

    public Debug(@NotNull TRSelf player) {
        super("Debug", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) return;

        PlayerMove.strafe(Advanced3Config.debugDouble);
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.debugEnabled;
    }
}
