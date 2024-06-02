package top.infsky.cheatdetector.impl.checks.aim;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.config.AntiCheatConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.utils.TRPlayer;

import java.util.Set;

public class AimB extends Check {
    public static final Set<Integer> STEP = Set.of(25, 40, 45, 60, 90, 120, 135, 180);
    public AimB(@NotNull TRPlayer player) {
        super("AimB", player);
    }

    @Override
    public void _onTick() {
        boolean flagPitch = false;
        boolean flagYaw = false;
        float stepPitch = 0, stepYaw = 0;
        for (int step : STEP) {
            if (Math.abs(player.lastRot.x - player.currentRot.x) - step < AdvancedConfig.aimBMinDiffPitch) {
                flagPitch = true;
                stepPitch = player.lastRot.x - player.currentRot.x;
            }
            if (Math.abs(player.lastRot.y - player.currentRot.y) - step < AdvancedConfig.aimBMinDiffYaw) {
                flagYaw = true;
                stepYaw = player.lastRot.y - player.currentRot.y;
            }
            if (flagPitch && flagYaw) break;
        }

        if (flagPitch && flagYaw) {
            flag("perfect step aim. deltaYaw: %.1f  deltaPitch: %.1f".formatted(stepYaw, stepPitch));
        } else if (flagPitch) {
            flag("perfect pitch step aim. deltaPitch: %.1f".formatted(stepPitch));
        } else if (flagYaw) {
            flag("perfect pitch step aim. deltaYaw: %.1f".formatted(stepYaw));
        }
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.aimBAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.aimBCheck || !AntiCheatConfig.experimentalCheck;
    }
}
