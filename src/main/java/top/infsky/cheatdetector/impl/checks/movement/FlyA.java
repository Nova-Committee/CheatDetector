package top.infsky.cheatdetector.impl.checks.movement;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.impl.utils.world.PlayerMove;
import top.infsky.cheatdetector.utils.TRPlayer;

public class FlyA extends Check {
    public FlyA(@NotNull TRPlayer player) {
        super("FlyA", player);
    }

    @Override
    public void _onTick() {
        if (player.fabricPlayer.isPassenger() || !PlayerMove.isMove(player.currentMotion)) return;

        if (player.lastMotion.y() == 0 && player.currentMotion.y() == 0) {
            flag("Invalid Y-motion.  onGround=%s".formatted(player.currentOnGround));
        }
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.flyAAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.flyACheck;
    }
}
