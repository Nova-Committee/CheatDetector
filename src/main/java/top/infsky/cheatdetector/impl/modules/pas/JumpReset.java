package top.infsky.cheatdetector.impl.modules.pas;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.impl.utils.world.VelocityUtils;
import top.infsky.cheatdetector.config.ModuleConfig;

public class JumpReset extends Module {
    @Getter
    @Nullable
    private static Module instance = null;

    private boolean isHurt = false;
    private boolean bypassOne = false;

    public JumpReset(@NotNull TRSelf player) {
        super("JumpReset", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) return;

        if (VelocityUtils.shouldBypassNextHurt(player))
            bypassOne = true;
        if (player.fabricPlayer.hurtTime > 0) {
            if (!isHurt) {
                isHurt = true;

                if (bypassOne) {
                    bypassOne = false;
                    return;
                }
                if (!VelocityUtils.shouldCheck(player, null)) return;

                if (player.lastOnGround) {
                    player.fabricPlayer.jumpFromGround();
                }
            }
        } else {
            isHurt = false;
        }
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.jumpResetEnabled || !ModuleConfig.aaaPASModeEnabled;
    }
}
