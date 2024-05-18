package top.infsky.cheatdetector.impl.modules.common;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.mixins.LivingEntityAccessor;
import top.infsky.cheatdetector.utils.TRSelf;

public class NoJumpDelay extends Module {
    public NoJumpDelay(@NotNull TRSelf player) {
        super("NoJumpDelay", player);
    }

    @Override
    public void _onTick() {
        if (isDisabled()) return;

        ((LivingEntityAccessor) player.fabricPlayer).setJumpDelay(0);
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.noJumpDelayEnabled;
    }
}
