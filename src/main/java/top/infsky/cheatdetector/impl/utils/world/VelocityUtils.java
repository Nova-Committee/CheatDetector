package top.infsky.cheatdetector.impl.utils.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.cheatdetector.utils.TRPlayer;

import java.util.List;
import java.util.Set;

public class VelocityUtils {
    public static final List<Direction> HORIZON_DIRECTION = List.of(
            Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH
    );
    public static final List<MobEffect> HURT_EFFECTS = List.of(
            MobEffects.WITHER, MobEffects.POISON, MobEffects.HARM
    );

    public enum VelocityDirection {
        HORIZON,
        VERTICAL,
        ALL
    }
    
    public static boolean shouldCheck(@NotNull TRPlayer player, @Nullable VelocityDirection velocityDirection) {
        final Set<MobEffect> hasEffects = player.fabricPlayer.getActiveEffectsMap().keySet();

        // passenger
        if (player.fabricPlayer.isPassenger())
            return false;

        // fire
        if ((player.fabricPlayer.isOnFire() || player.fabricPlayer.isInLava()) && !hasEffects.contains(MobEffects.FIRE_RESISTANCE))
            return false;

        // asphyxia
        if (player.fabricPlayer.isInWater() && !hasEffects.contains(MobEffects.WATER_BREATHING))
            return false;

        // wall
        if (player.fabricPlayer.isInWall())
            return false;

        // effect
        for (MobEffect effect : HURT_EFFECTS) {
            if (hasEffects.contains(effect))
                return false;
        }

        // MagmaBlock
        if (player.fabricPlayer.getBlockStateOn().is(Blocks.MAGMA_BLOCK))
            return false;

        Level level = LevelUtils.getClientLevel();

        // hunger
        if (player.fabricPlayer.getFoodData().getFoodLevel() == 0 && level.getDifficulty() == Difficulty.HARD)
            return false;

        // has block
        if (velocityDirection != null) {
            final BlockPos feetBlock = player.fabricPlayer.blockPosition();
            switch (velocityDirection) {
                case ALL: {
                }
                case HORIZON: {
                    for (Direction direction : HORIZON_DIRECTION) {
                        if (!level.getBlockState(feetBlock.relative(direction)).isAir())
                            return false;
                    }
                    if (velocityDirection == VelocityDirection.HORIZON) break;
                }
                case VERTICAL: {
                    if (!level.getBlockState(feetBlock.above()).isAir()
                            || !level.getBlockState(feetBlock).isAir()
                            || !level.getBlockState(feetBlock.above()).isAir()) {
                        return false;
                    }
                    break;
                }
            }
        }

        // as default
        return true;
    }

    public static boolean shouldBypassNextHurt(@NotNull TRPlayer player) {
        final Set<MobEffect> hasEffects = player.fabricPlayer.getActiveEffectsMap().keySet();

        return player.lastFallDistance > 3 && !hasEffects.contains(MobEffects.SLOW_FALLING);
    }
}
