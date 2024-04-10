package top.infsky.cheatdetector.anticheat.checks;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

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
    protected long getAlertBuffer() {
        return CONFIG().getAdvanced().getFlightBAlertBuffer();
    }

    @Override
    protected boolean isDisabled() {
        return !CONFIG().getAdvanced().isFlightBCheck();
    }
}
