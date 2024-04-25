package top.infsky.cheatdetector.anticheat.checks;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.anticheat.utils.PlayerMove;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.config.AntiCheatConfig;

public class SpeedA extends Check {
    public boolean hasJumped = false;
    public short jumpTick = 0;
    public SpeedA(@NotNull TRPlayer player) {
        super("SpeedA", player);
    }

    @Override
    public void _onTick() {
        if (hasJumped && !player.jumping) {
            hasJumped = false;
            jumpTick = AdvancedConfig.getSpeedAAfterJumpJumpTick();
            return;
        }

        if (jumpTick > 0) jumpTick--;

        // check if player is on ground (not in liquid or in water)
        if (player.lastPos == null || player.hasSetback || !player.fabricPlayer.onGround() || !player.lastOnGround || player.fabricPlayer.isInWater()) return;

        double maxSecSpeed;
        if (jumpTick > 0)
            maxSecSpeed = AdvancedConfig.speedAAfterJumpSpeed;
        else if (player.fabricPlayer.isSprinting())
            maxSecSpeed = AdvancedConfig.speedASprintingSpeed;
        else if (player.fabricPlayer.isSilent())
            maxSecSpeed = AdvancedConfig.speedASilentSpeed;
        else  // walking
            maxSecSpeed = AdvancedConfig.speedAWalkSpeed;

        final double speed = PlayerMove.getXzSecSpeed(player.lastPos, player.currentPos);
        final double possibleSpeed = maxSecSpeed * player.speedMul + AntiCheatConfig.threshold;
        if (speed > possibleSpeed) {
            flag(String.format("Current: %.2f  Max: %.2f", speed, possibleSpeed));
        }
    }

    @Override
    public void _onJump() {
        hasJumped = true;
    }

    @Override
    protected int getAlertBuffer() {
        return AdvancedConfig.speedAAlertBuffer;
    }

    @Override
    protected boolean isDisabled() {
        return !AdvancedConfig.speedACheck;
    }
}
