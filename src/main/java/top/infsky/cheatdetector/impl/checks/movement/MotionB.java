package top.infsky.cheatdetector.impl.checks.movement;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.impl.utils.world.PlayerMove;
import top.infsky.cheatdetector.utils.TRPlayer;

public class MotionB extends Check {
    public MotionB(@NotNull TRPlayer player) {
        super("MotionB", player);
    }

    @Override
    public void _onTick() {
        if (isDisabled()) return;

        if (player.currentOnGround
                || MotionA.IGNORED_EFFECTS.stream().anyMatch(effect -> player.fabricPlayer.hasEffect(effect))
                || player.fabricPlayer.isPassenger()
                || player.fabricPlayer.isInWater()
                || player.fabricPlayer.isFallFlying()
                || player.fabricPlayer.onClimbable() || player.fabricPlayer.hurtTime > 0
        ) return;

        double shouldMotion = PlayerMove.predictedMotion(player.lastMotion.y(), 1);
        if (Math.abs(player.currentMotion.y() - shouldMotion) > 0.01) {
            flag("invalid jump motion. motion: %.2f  should: %.2f:".formatted(
                    player.currentMotion.y(),
                    shouldMotion
            ));
        }
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.motionBAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.motionBCheck;
    }
}
