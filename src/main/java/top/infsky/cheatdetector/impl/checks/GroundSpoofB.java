package top.infsky.cheatdetector.impl.checks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.impl.utils.world.BlockUtils;
import top.infsky.cheatdetector.utils.TRPlayer;

import java.util.List;

public class GroundSpoofB extends Check {
    public GroundSpoofB(@NotNull TRPlayer player) {
        super("GroundSpoofB", player);
    }

    @Override
    public void _onTick() {
        final Level level = CheatDetector.CLIENT.level;
        if (level == null) return;

        if (!player.currentOnGround && Math.floor(player.currentPos.y()) == player.currentPos.y()) {  // check if it's *OnGround*
            final BlockPos groundPos = player.fabricPlayer.blockPosition().below();

            if (check(level, groundPos)) {
                flag();
                setback();
            }
        }
    }

    public void setback() {
        player.currentOnGround = true;
    }

    public static boolean check(@NotNull Level level, @NotNull BlockPos groundPos) {
        if (!BlockUtils.isFullBlock(level.getBlockState(groundPos)) || !BlockUtils.isFullBlock(level.getBlockState(groundPos.above())))
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
            if (BlockUtils.isFullBlock(level.getBlockState(blockPos))) {
                count++;
            }
        }

        return count >= 8;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.groundSpoofBCheck;
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.groundSpoofBAlertBuffer;
    }
}
