package top.infsky.cheatdetector.impl.checks;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.config.AntiCheatConfig;


public class BlinkA extends Check {
    
    public BlinkA(@NotNull TRPlayer player) {
        super("BlinkA", player);
    }

    @Override
    public void _onTick() {
        if (player.lastPos == null || player.hasSetback) return;

        if (player.lastPos.distanceTo(player.currentPos) > (
                AdvancedConfig.blinkMaxDistance * player.speedMul + player.fabricPlayer.fallDistance + AntiCheatConfig.threshold)) {
            flag();
        }
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.blinkAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.blinkCheck;
    }
}
