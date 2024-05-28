package top.infsky.cheatdetector.impl.checks.movement;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.utils.TRPlayer;

import java.util.List;

public class FlyC extends Check {
    public static final List<Block> IGNORED_BLOCKS = List.of(Blocks.COBWEB, Blocks.WATER, Blocks.LAVA, Blocks.POWDER_SNOW);
    public FlyC(@NotNull TRPlayer player) {
        super("FlyC", player);
    }

    @Override
    public void _onTick() {
        if (player.fabricPlayer.isPassenger()
                || player.currentOnGround || player.fabricPlayer.isFallFlying()
                || IGNORED_BLOCKS.stream().anyMatch(block -> player.fabricPlayer.getFeetBlockState().is(block))) return;

        int repeatFromTick = 0;
        for (int i = 0; i < player.motionHistory.size(); i++) {
            if (Math.abs(player.motionHistory.get(i).y() - player.currentMotion.y()) >= AdvancedConfig.flyCMinDiffYMotion) {
                return;
            }

            repeatFromTick++;
        }

        if (repeatFromTick >= AdvancedConfig.flyCMinRepeatTicks) {
            flag("Repeat Y-motion from %s ticks.".formatted(repeatFromTick));
        }
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.flyCAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.flyCCheck;
    }
}
