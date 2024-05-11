package top.infsky.cheatdetector.impl.checks;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.utils.TRPlayer;

public class SpeedC extends Check {
    public SpeedC(@NotNull TRPlayer player) {
        super("SpeedC", player);
    }

    @Override
    public void _onTick() {
        if (player.fabricPlayer.isSprinting() && player.lastPos.distanceTo(player.currentPos) == 0)
            flag();
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.speedCAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.speedCCheck;
    }
}
