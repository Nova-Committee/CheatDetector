package top.infsky.cheatdetector.impl.checks;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.config.AdvancedConfig;


public class FlightB extends Check {
    public boolean hasFlag = false;

    public FlightB(@NotNull TRPlayer player) {
        super("FlightB", player);
    }

    @Override
    public void _onTick() {
        final boolean mayfly = player.fabricPlayer.getAbilities().mayfly;
        final boolean flying = player.fabricPlayer.getAbilities().flying;
        if (mayfly || flying) {
            if (!hasFlag) {
                flag(String.format("mayfly: %s  flying: %s", mayfly, flying));
                hasFlag = true;
            }
        } else {
            hasFlag = false;
        }
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.flightBAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.flightBCheck;
    }
}
