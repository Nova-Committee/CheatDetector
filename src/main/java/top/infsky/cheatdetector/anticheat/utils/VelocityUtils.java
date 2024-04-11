package top.infsky.cheatdetector.anticheat.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.MagmaBlock;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.anticheat.TRPlayer;

import java.util.List;
import java.util.Set;

public class VelocityUtils {
    public static final List<Direction> HORIZON_DIRECTION = List.of(Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH);
    public static final List<MobEffect> HURT_EFFECTS = List.of(
            MobEffects.WITHER,
            MobEffects.POISON
    );
    public enum VelocityDirection {
        HORIZON,
        VERTICAL,
        ALL
    }
    
    public static boolean shouldCheck(@NotNull TRPlayer player, @NotNull VelocityDirection velocityDirection) {
        final Set<MobEffect> hasEffects = player.fabricPlayer.getActiveEffectsMap().keySet();

        // fall
        if (player.lastFallDistance > 3 && !hasEffects.contains(MobEffects.SLOW_FALLING))
            return false;

        // fire
        if ((player.fabricPlayer.isOnFire() || player.fabricPlayer.isInLava()) && !hasEffects.contains(MobEffects.FIRE_RESISTANCE))
            return false;

        // asphyxia
        if (player.fabricPlayer.isInWater() && !hasEffects.contains(MobEffects.WATER_BREATHING))
            return false;

        // hunger
        if (CheatDetector.CLIENT.level != null
                && player.fabricPlayer.getFoodData().getFoodLevel() == 0 && CheatDetector.CLIENT.level.getDifficulty() == Difficulty.HARD)
            return false;

        // MagmaBlock
        if (CheatDetector.CLIENT.level != null
                && CheatDetector.CLIENT.level.getBlockState(player.fabricPlayer.blockPosition().below()).getBlock() instanceof MagmaBlock)
            return false;

        // wall
        if (player.fabricPlayer.isInWall())
            return false;
        if (!player.fabricPlayer.getFeetBlockState().isAir())
            return false;

        // effect
        for (MobEffect effect : HURT_EFFECTS) {
            if (hasEffects.contains(effect))
                return false;
        }

        // has block
        if (CheatDetector.CLIENT.level != null) {
            final Level level = CheatDetector.CLIENT.level;
            final BlockPos feetBlock = player.fabricPlayer.blockPosition();
            switch (velocityDirection) {
                case ALL: {}
                case HORIZON: {
                    for (Direction direction : HORIZON_DIRECTION) {
                        if (!level.getBlockState(feetBlock.relative(direction)).isAir())
                            return false;
                    }
                    if (velocityDirection == VelocityDirection.HORIZON) break;
                }
                case VERTICAL: {
                    if (!level.getBlockState(feetBlock.above()).isAir()
                            || !level.getBlockState(feetBlock).isAir()) {
                        return false;
                    }
                    break;
                }
            }
        }

        // as default
        return true;
    }
}
