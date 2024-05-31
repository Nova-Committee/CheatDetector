package top.infsky.cheatdetector.impl.checks.movement;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.impl.utils.world.PlayerMove;
import top.infsky.cheatdetector.utils.TRPlayer;

import java.util.List;

public class FlyC extends Check {
    public static final List<Block> IGNORED_BLOCKS = List.of(Blocks.COBWEB, Blocks.WATER, Blocks.LAVA, Blocks.POWDER_SNOW, Blocks.SLIME_BLOCK, Blocks.SOUL_SAND);
    public FlyC(@NotNull TRPlayer player) {
        super("FlyC", player);
    }

    @Override
    public void _onTick() {
        if (player.fabricPlayer.isPassenger()
                || player.currentOnGround || player.fabricPlayer.isFallFlying()
//                || player.fabricPlayer.hurtTime > 0
                || IGNORED_BLOCKS.stream().anyMatch(block -> player.fabricPlayer.getFeetBlockState().is(block))) return;

        if (PlayerMove.isInvalidMotion(player.currentMotion)) return;


        List<Vec3> posDiff = PlayerMove.getPosHistoryDiff(player.posHistory);
        int repeatFromTick = 0;
        for (Vec3 diff : posDiff) {
            if (diff.y() == posDiff.get(0).y()) {
                repeatFromTick++;
            }
        }

        if (repeatFromTick >= AdvancedConfig.flyCMinRepeatTicks) {
            flag("Repeat Y-diff from %s ticks: %.2f".formatted(repeatFromTick, posDiff.get(0).y()));
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
