package top.infsky.cheatdetector.impl.checks.aim;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.config.AntiCheatConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.impl.utils.world.LevelUtils;
import top.infsky.cheatdetector.impl.utils.world.PlayerMove;
import top.infsky.cheatdetector.impl.utils.world.PlayerRotation;
import top.infsky.cheatdetector.utils.TRPlayer;

import java.util.List;

public class AimA extends Check {
    public AimA(@NotNull TRPlayer player) {
        super("AimA", player);
    }

    @Override
    public void _onTick() {
        if (player.currentRot.equals(player.lastRot)) return;
        if (AdvancedConfig.aimAOnlyOnSwing && !(!player.lastSwing && player.currentSwing)) return;

        float deltaYaw = player.currentRot.y - player.lastRot.y;
        float deltaPitch = player.currentRot.x - player.lastRot.x;

        if (deltaYaw < AdvancedConfig.aimAMinDeltaYaw || deltaPitch < AdvancedConfig.aimAMinDeltaPitch) return;

        List<LivingEntity> possibleTargets = LevelUtils.getEntities().stream()
                .filter(entity -> !AdvancedConfig.aimAOnlyPlayer || entity instanceof Player)
                .filter(entity -> !entity.is(player.fabricPlayer))
                .filter(entity -> !AdvancedConfig.aimAOnlyIfTargetIsMoving || !PlayerMove.isNoMove(entity.getDeltaMovement()))
                .filter(entity -> entity.distanceTo(player.fabricPlayer) <= AdvancedConfig.aimAMaxDistance)
                .toList();

        double diffYaw = 0;
        double diffPitch = 0;
        boolean flagYaw = false;
        boolean flagPitch = false;
        LivingEntity flagTarget = null;
        for (LivingEntity entity : possibleTargets) {
            diffYaw = Math.abs(PlayerRotation.getYaw(entity.getEyePosition()) - entity.getYRot());
            diffPitch = Math.abs(PlayerRotation.getPitch(entity.getEyePosition()) - entity.getXRot());

            flagYaw = diffYaw < AdvancedConfig.aimAMinDiffYaw;
            flagPitch = diffPitch < AdvancedConfig.aimAMinDiffPitch;

            if (flagPitch || flagYaw) {
                flagTarget = entity;
                break;
            }
        }

        if (flagYaw && flagPitch) {
            flag("Too small deviation. target: %s  diff: %.2f,%.2f  delta: %.2f,%.2f".formatted(
                    flagTarget.getName().getString(), diffYaw, diffPitch, deltaYaw, deltaPitch)
            );
        } else if (flagYaw) {
            flag("Too small yaw deviation. target: %s  diff: %.2f  delta: %.2f".formatted(
                    flagTarget.getName().getString(), diffYaw, deltaYaw)
            );
        } else if (flagPitch) {
            flag("Too small pitch deviation. target: %s  diff: %.2f  delta: %.2f".formatted(
                    flagTarget.getName().getString(), diffPitch, deltaPitch)
            );
        }
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.aimAAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.aimACheck || !AntiCheatConfig.experimentalCheck;
    }
}
