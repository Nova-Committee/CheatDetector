package top.infsky.cheatdetector.anticheat.checks;

import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

public class SpeedB extends Check {
    public SpeedB(TRPlayer player) {
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
    protected long getAlertBuffer() {
        return CONFIG().getAdvanced().getSpeedBAlertBuffer();
    }

    @Override
    protected boolean isDisabled() {
        return !CONFIG().getAdvanced().isSpeedBCheck();
    }
}
