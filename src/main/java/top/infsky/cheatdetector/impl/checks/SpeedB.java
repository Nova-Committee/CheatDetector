package top.infsky.cheatdetector.impl.checks;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.config.AdvancedConfig;

public class SpeedB extends Check {
    public SpeedB(@NotNull TRPlayer player) {
        super("SpeedB", player);
    }

    @Override
    public void _onTick() {
        if (player.fabricPlayer.isSprinting() && player.fabricPlayer.getFoodData().getFoodLevel() <= 6) {
            flag();
        }
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.speedBAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.speedBCheck;
    }
}
