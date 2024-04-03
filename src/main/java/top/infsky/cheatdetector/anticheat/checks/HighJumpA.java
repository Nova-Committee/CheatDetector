package top.infsky.cheatdetector.anticheat.checks;

import lombok.val;
import net.minecraft.world.phys.Vec3;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

public class HighJumpA extends Check {
    public double highestY = Double.MIN_VALUE;
    public HighJumpA(TRPlayer player) {
        super("HighJumpA", player);
    }

    @Override
    public void _onTick() {
        if (player.isJumping() && player.lastOnGroundPos != player.lastOnLiquidGroundPos && !(player.fabricPlayer.hurtTime > 0)) {
            if (player.currentPos.y() > highestY) highestY = player.currentPos.y();

            val groundPrefixPos = new Vec3(0, player.lastOnGroundPos.y(), 0);
            val airPrefixPos = new Vec3(0, highestY, 0);

            final double jumpDistance = airPrefixPos.distanceTo(groundPrefixPos);
            if (jumpDistance > 1.25219 * (1 + player.fabricPlayer.getJumpBoostPower()) + CONFIG().getThreshold()) {
                flag(String.valueOf(jumpDistance));
            }
        } else highestY = Double.MIN_VALUE;
    }
}
