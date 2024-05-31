package top.infsky.cheatdetector.impl.utils.world;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.utils.TRSelf;

import java.util.*;

public class BlockUtils {
    public static Direction getPlaceSide(@NotNull BlockPos blockPos) {
        assert TRSelf.CLIENT.level != null;

        Vec3 lookVec = blockPos.getCenter().subtract(TRSelf.getInstance().fabricPlayer.getEyePosition());
        double bestRelevancy = -Double.MAX_VALUE;
        Direction bestSide = Direction.DOWN;

        for (Direction side : Direction.values()) {
            BlockPos neighbor = blockPos.relative(side);
            BlockState state = TRSelf.CLIENT.level.getBlockState(neighbor);

            // Check if neighbour isn't empty
            if (state.isAir() || isClickable(state.getBlock())) continue;

            // Check if neighbour is a fluid
            if (!state.getFluidState().isEmpty()) continue;

            double relevancy = side.getAxis().choose(lookVec.x(), lookVec.y(), lookVec.z()) * side.getAxisDirection().getStep();
            if (relevancy > bestRelevancy) {
                bestRelevancy = relevancy;
                bestSide = side;
            }
        }

        return bestSide;
    }

    public static boolean isClickable(Block block) {
        return block instanceof CraftingTableBlock
                || block instanceof AnvilBlock
                || block instanceof LoomBlock
                || block instanceof CartographyTableBlock
                || block instanceof GrindstoneBlock
                || block instanceof StonecutterBlock
                || block instanceof ButtonBlock
                || block instanceof BasePressurePlateBlock
                || block instanceof BaseEntityBlock
                || block instanceof BedBlock
                || block instanceof FenceGateBlock
                || block instanceof DoorBlock
                || block instanceof NoteBlock
                || block instanceof TrapDoorBlock;
    }

    public static boolean isFullBlock(@NotNull BlockState blockState) {
        return blockState.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
    }

    public static @NotNull List<BlockPos> getAllInBox(@NotNull BlockPos from, @NotNull BlockPos to)
    {
        ArrayList<BlockPos> blocks = new ArrayList<>();

        BlockPos min = new BlockPos(Math.min(from.getX(), to.getX()),
                Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        BlockPos max = new BlockPos(Math.max(from.getX(), to.getX()),
                Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));

        for (int x = min.getX(); x <= max.getX(); x++)
            for (int y = min.getY(); y <= max.getY(); y++)
                for (int z = min.getZ(); z <= max.getZ(); z++)
                    blocks.add(new BlockPos(x, y, z));

        return blocks;
    }

    public static @NotNull Set<BlockPos> getSurroundBlocks(@NotNull AbstractClientPlayer target) {
        AABB playerBox = target.getBoundingBox();

        int minX = Mth.floor(playerBox.minX) - 1;
        int minY = Mth.floor(playerBox.minY) - 1;
        int minZ = Mth.floor(playerBox.minZ) - 1;
        int maxX = Mth.floor(playerBox.maxX) + 1;
        int maxY = Mth.floor(playerBox.maxY) + 1;
        int maxZ = Mth.floor(playerBox.maxZ) + 1;

        return new HashSet<>(
                getAllInBox(new BlockPos(minX, minY, minZ), new BlockPos(maxX, maxY, maxZ))
                        .stream()
                        .filter(blockPos -> !playerBox.intersects(new AABB(blockPos)))
                        .filter(blockPos -> !((blockPos.getX() == minX || blockPos.getX() == maxX) && (blockPos.getZ() == minZ || blockPos.getZ() == maxZ)))
                        .filter(blockPos -> !((blockPos.getY() == minY || blockPos.getY() == maxY)
                                && (blockPos.getX() == minX || blockPos.getX() == maxX || blockPos.getZ() == minZ || blockPos.getZ() == maxZ)))
                        .toList()
        );
    }
}
