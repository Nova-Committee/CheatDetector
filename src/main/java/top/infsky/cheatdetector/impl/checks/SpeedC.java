package top.infsky.cheatdetector.impl.checks;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.utils.TRPlayer;

public class SpeedC extends Check {
    public SpeedC(@NotNull TRPlayer player) {
        super("SpeedC", player);
    }

    @Override
    public void _onTick() {
        if (player.fabricPlayer.isSprinting()) {
            double distance = player.lastPos.distanceTo(player.currentPos);
            Vec3 motion = player.fabricPlayer.getDeltaMovement();
            if (distance == 0)
                flag(distance, motion);
        }
    }

    public void flag(double distance, @NotNull Vec3 motion) {
        flag("distance: %.1f, motion: %.2f,%.2f,%.2f".formatted(distance, motion.x(), motion.y(), motion.z()));
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
