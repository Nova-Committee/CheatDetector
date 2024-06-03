package top.infsky.cheatdetector.impl.checks.movement;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.utils.TRPlayer;

import java.util.List;
import java.util.function.Predicate;


public class BoatFlyA extends Check {
    public static final List<Block> IGNORE_BLOCKS = List.of(Blocks.WATER, Blocks.PISTON, Blocks.PISTON_HEAD, Blocks.MOVING_PISTON, Blocks.STICKY_PISTON);
    public BoatFlyA(@NotNull TRPlayer player) {
        super("BoatFlyA", player);
    }

    @Override
    public void _onTick() {
        if (player.fabricPlayer.getVehicle() instanceof Boat boat) {
            if (boat.onGround()) return;
            if (boat.isInWater()) {
                if (blockCheck(boat, blockState -> blockState.is(Blocks.BUBBLE_COLUMN) && !blockState.getValue(BubbleColumnBlock.DRAG_DOWN)))
                    return;
            } else {
                if (IGNORE_BLOCKS.stream().anyMatch(block -> blockCheck(boat, blockState -> blockState.is(block))))
                    return;
            }

            if (player.currentVehicleMotion.y() >= 0.01) {
                flag("Invalid boat Y-motion: %s  inWater=%s onGround=%s".formatted(player.currentVehicleMotion.y(), boat.isInWater(), boat.onGround()));
            }
        }
    }

    private static boolean blockCheck(@NotNull Boat boat, @NotNull Predicate<BlockState> predicate) {
        return predicate.test(boat.getFeetBlockState()) || predicate.test(boat.getBlockStateOn());
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.boatFlyAAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.boatFlyACheck;
    }
}
