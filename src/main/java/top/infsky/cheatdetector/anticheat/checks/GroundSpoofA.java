package top.infsky.cheatdetector.anticheat.checks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;

import java.util.List;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

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

        if (player.lastOnGround2 && player.lastOnGround && player.currentOnGround) {  // check if it's legit
            final BlockPos groundPos = player.fabricPlayer.blockPosition().below();

            if (check(level, groundPos)) {
                flag();
                setback();
            }
        }
    }

    public void setback() {
        player.currentOnGround = false;
    }

    public static boolean check(@NotNull Level level, BlockPos groundPos) {
        if (!level.getBlockState(groundPos).isAir())
            return false;

        short count = 0;
        final List<BlockPos> blocks = List.of(
                groundPos.east(),
                groundPos.east().north(),
                groundPos.west(),
                groundPos.west().south(),
                groundPos.north(),
                groundPos.north().west(),
                groundPos.south(),
                groundPos.south().east()
        );
        for (BlockPos blockPos : blocks) {
            if (level.getBlockState(blockPos).isAir()) {
                count++;
            }
        }
        return count >= 8;
    }

    @Override
    public boolean isDisabled() {
        return !CONFIG().getAdvanced().isGroundSpoofACheck();
    }

    @Override
    public long getAlertBuffer() {
        return CONFIG().getAdvanced().getGroundSpoofAAlertBuffer();
    }
}
