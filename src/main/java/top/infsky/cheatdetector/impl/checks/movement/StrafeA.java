package top.infsky.cheatdetector.impl.checks.movement;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.impl.utils.world.PlayerMove;
import top.infsky.cheatdetector.utils.TRPlayer;

public class StrafeA extends Check {
    private Vec3 futureMotion = null;

    public StrafeA(@NotNull TRPlayer player) {
        super("StrafeA", player);
    }

    @Override
    public void _onTick() {
        if (player.currentOnGround || PlayerMove.isNoMove(player.currentMotion) || player.lastRot.equals(player.currentRot)) {
            futureMotion = null;
            return;
        }

        if (PlayerMove.isInvalidMotion(player.currentMotion)) return;

        if (futureMotion != null) {
            double diff = PlayerMove.getMaxXZDiff(player.currentMotion, futureMotion);
            if (diff <= AdvancedConfig.strafeAMaxDiffToFlag) {
                flag("Strafe in air. diff:%.4f".formatted(diff));
            }
        }

        futureMotion = PlayerMove.getStrafeMotion(
                PlayerMove.speed(player.fabricPlayer),
                PlayerMove.direction(1, 0, player.fabricPlayer.yRotO + (player.fabricPlayer.getYRot() - player.fabricPlayer.yRotO) * 50),
                player.currentMotion.y()
        );
    }



    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.strafeAAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.strafeACheck;
    }
}
