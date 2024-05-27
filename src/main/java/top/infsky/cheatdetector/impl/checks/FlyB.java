package top.infsky.cheatdetector.impl.checks;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.utils.TRPlayer;

public class FlyB extends Check {
    public FlyB(@NotNull TRPlayer player) {
        super("FlyB", player);
    }

    @Override
    public void _onTick() {
        if (player.fabricPlayer.isSwimming() && !player.fabricPlayer.isInWater()) {
            flag();
        }
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.flyBAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.flyBCheck;
    }
}
