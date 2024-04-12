package top.infsky.cheatdetector.anticheat.checks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;

/**
 * 不稳定
 */
public class GroundSpoofA extends Check {
    public GroundSpoofA(@NotNull TRPlayer player) {
        super("GroundSpoofA", player);
    }

    @Override
    public void _onTick() {
        final Level level = CheatDetector.CLIENT.level;
        if (level == null) return;

        if (!player.lastOnGround2 && player.currentOnGround) {  // check if it's legit
            final BlockPos groundPos = player.fabricPlayer.blockPosition().below();

            if (level.getBlockState(groundPos).isAir()) {
                flag();
                setback();
            }
        }
    }

    public void setback() {
        player.currentOnGround = false;
    }
}
