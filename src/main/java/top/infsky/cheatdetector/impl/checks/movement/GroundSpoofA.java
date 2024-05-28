package top.infsky.cheatdetector.impl.checks.movement;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.config.AdvancedConfig;

import java.util.List;

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

    public static boolean check(@NotNull Level level, @NotNull BlockPos groundPos) {
        if (!level.getBlockState(groundPos).isAir() || !level.getBlockState(groundPos.above()).isAir())
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
        final List<BlockPos> scaffolding = List.of(
                groundPos.above().east(),
                groundPos.above().east().north(),
                groundPos.above().west(),
                groundPos.above().west().south(),
                groundPos.above().north(),
                groundPos.above().north().west(),
                groundPos.above().south(),
                groundPos.above().south().east()
        );

        for (BlockPos blockPos : blocks) {
            if (level.getBlockState(blockPos).isAir()) {
                count++;
            }
        }
        for (BlockPos blockPos : scaffolding) {
            if (level.getBlockState(blockPos).is(Blocks.SCAFFOLDING)) return false;
        }

        return count >= 8;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.groundSpoofACheck;
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.groundSpoofAAlertBuffer;
    }
}
