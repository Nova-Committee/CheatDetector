package top.infsky.cheatdetector.impl.utils.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public record BlockUpdate(BlockPos blockPos, BlockState blockState) {
}
