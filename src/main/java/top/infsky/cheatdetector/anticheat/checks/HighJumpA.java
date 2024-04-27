package top.infsky.cheatdetector.anticheat.checks;

import lombok.val;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.anticheat.utils.PlayerMove;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.config.AntiCheatConfig;

public class HighJumpA extends Check {
    public double highestY = 0;
    public boolean hasFlag = false;
    public HighJumpA(@NotNull TRPlayer player) {
        super("HighJumpA", player);
    }

    @Override
    public void _onTick() {
        if (player.fabricPlayer.isFallFlying()) return;
        if (player.fabricPlayer.isFallFlying()) {
            hasFlag = true;
            return;
        }

        if (player.isJumping() && player.lastOnGroundPos != player.lastOnLiquidGroundPos && !(player.fabricPlayer.hurtTime > 0) && !player.fabricPlayer.isPassenger()) {
            if (player.currentPos.y() > highestY) highestY = player.currentPos.y();

            val groundPrefixPos = new Vec3(0, player.lastOnGroundPos.y(), 0);
            val airPrefixPos = new Vec3(0, highestY, 0);

            final double jumpDistance = airPrefixPos.distanceTo(groundPrefixPos);
            final double possibleDistance = 1 + PlayerMove.getJumpDistance(player.fabricPlayer) + AntiCheatConfig.threshold;
            if (!hasFlag && jumpDistance > possibleDistance) {
                flag(String.format("Current: %.2f  Max: %.2f", jumpDistance, possibleDistance));
                hasFlag = AdvancedConfig.highJumpAFlagOne;
            }
        } else {
            hasFlag = false;
            highestY = 0;
        }
    }

    @Override
    protected int getAlertBuffer() {
        return AdvancedConfig.highJumpAAlertBuffer;
    }

    @Override
    protected boolean isDisabled() {
        return !AdvancedConfig.highJumpACheck;
    }
}
