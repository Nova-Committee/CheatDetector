package top.infsky.cheatdetector.anticheat.checks;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

public class BlinkA extends Check {
    
    public BlinkA(@NotNull TRPlayer player) {
        super("BlinkA", player);
    }

    @Override
    public void _onTick() {
        if (player.lastPos == null || player.hasSetback) return;

        if (player.lastPos.distanceTo(player.currentPos) > (
                CONFIG().getAdvanced().getBlinkMaxDistance() * player.speedMul + player.fabricPlayer.fallDistance + CONFIG().getAntiCheat().getThreshold())) {
            flag();
        }
    }

    @Override
    protected long getAlertBuffer() {
        return CONFIG().getAdvanced().getBlinkAlertBuffer();
    }

    @Override
    protected boolean isDisabled() {
        return !CONFIG().getAdvanced().isBlinkCheck();
    }
}
