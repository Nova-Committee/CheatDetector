package top.infsky.cheatdetector.anticheat.checks;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.config.AdvancedConfig;

public class SpeedB extends Check {
    public SpeedB(@NotNull TRPlayer player) {
        super("SpeedB", player);
    }

    @Override
    public void _onTick() {
        if (player.fabricPlayer.isSprinting() && player.fabricPlayer.getFoodData().getFoodLevel() <= 6) {
            flag();
            player.fabricPlayer.setSprinting(false);
        }
    }

    @Override
    protected int getAlertBuffer() {
        return AdvancedConfig.speedBAlertBuffer;
    }

    @Override
    protected boolean isDisabled() {
        return !AdvancedConfig.speedBCheck;
    }
}
