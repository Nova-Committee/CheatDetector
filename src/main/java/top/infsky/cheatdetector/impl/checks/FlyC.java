package top.infsky.cheatdetector.impl.checks;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.utils.TRPlayer;

public class FlyC extends Check {
    public FlyC(@NotNull TRPlayer player) {
        super("FlyC", player);
    }

    @Override
    public void _onTick() {
        if (player.fabricPlayer.isSwimming() && !player.fabricPlayer.isInWater()) {
            flag();
        }
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.flyCAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.flyCCheck;
    }
}
