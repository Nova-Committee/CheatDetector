package top.infsky.cheatdetector.anticheat.checks;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;

public class FlightB extends Check {
    public boolean hasFlag = false;

    public FlightB(@NotNull TRPlayer player) {
        super("FlightB", player);
    }

    @Override
    public void _onTick() {
        if ((player.fabricPlayer.getAbilities().mayfly || player.fabricPlayer.getAbilities().flying)
                && !hasFlag) {
            hasFlag = true;
            flag(true);
        } else {
            hasFlag = false;
        }
    }
}
