package top.infsky.cheatdetector.impl.checks.movement;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.impl.utils.world.PlayerMove;
import top.infsky.cheatdetector.utils.TRPlayer;

import java.util.HashSet;
import java.util.Set;

public class StrafeA extends Check {
    private static final Set<Integer> movement = Set.of(1, 0, -1);
    private Set<Vec3> futureMotion = null;

    public StrafeA(@NotNull TRPlayer player) {
        super("StrafeA", player);
    }

    @Override
    public void _onTick() {
        if (player.currentOnGround || PlayerMove.isNoMove(player.currentMotion) || Math.abs(player.lastRot.y - player.currentRot.y) < 5
                || player.fabricPlayer.isFallFlying() || player.fabricPlayer.isPassenger()) {
            futureMotion = null;
            return;
        }

        if (PlayerMove.isInvalidMotion(player.currentMotion)) return;
        if (player.sprintHistory.subList(0, 10).stream().anyMatch(b -> b != player.currentSprint)) return;

        if (futureMotion != null) {
            for (Vec3 motion : futureMotion) {
                double diff = PlayerMove.getMaxXZDiff(player.currentMotion, motion);
                if (diff <= AdvancedConfig.strafeAMaxDiffToFlag) {
                    flag("Strafe in air. diff:%.4f".formatted(diff));
                }
            }
        }

        futureMotion = new HashSet<>();

        for (int forward : movement) {
            for (int strafe : movement) {
                Vec3 strafeMotion = PlayerMove.getStrafeMotion(
                        PlayerMove.speed(player.fabricPlayer),
                        PlayerMove.direction(forward, strafe, player.fabricPlayer.yRotO + (player.fabricPlayer.getYRot() - player.fabricPlayer.yRotO) * 50),
                        player.currentMotion.y()
                );
                futureMotion.add(strafeMotion);
            }
        }
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
