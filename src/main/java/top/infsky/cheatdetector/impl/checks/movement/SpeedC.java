package top.infsky.cheatdetector.impl.checks.movement;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.impl.utils.world.PlayerMove;
import top.infsky.cheatdetector.utils.TRPlayer;

public class SpeedC extends Check {
    public SpeedC(@NotNull TRPlayer player) {
        super("SpeedC", player);
    }

    @Override
    public void _onTick() {
        if (player.fabricPlayer.isSprinting() && !player.fabricPlayer.isSwimming() && !player.fabricPlayer.isPassenger()) {
            double speed = PlayerMove.getXzTickSpeed(player.lastPos, player.currentPos);
            if (speed == 0) {
                Vec3 motion = player.fabricPlayer.getDeltaMovement();
                flag("MotionX:%.2f MotionZ:%.2f".formatted(motion.x(), motion.z()));
            }
        }
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.speedCAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.speedCCheck;
    }
}
