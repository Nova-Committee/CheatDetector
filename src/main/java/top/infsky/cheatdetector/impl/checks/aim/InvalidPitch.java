package top.infsky.cheatdetector.impl.checks.aim;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.utils.TRPlayer;

public class InvalidPitch extends Check {
    public InvalidPitch(@NotNull TRPlayer player) {
        super("InvalidPitch", player);
    }

    @Override
    public void _onTick() {
        if (player.currentRot.x > 90 || player.currentRot.x < -90) {
            flag();
        }
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.invalidPitchAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.invalidPitchCheck;
    }
}
