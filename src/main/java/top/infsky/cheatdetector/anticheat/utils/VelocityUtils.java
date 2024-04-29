package top.infsky.cheatdetector.anticheat.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.MagmaBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.anticheat.TRPlayer;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class VelocityUtils {
    public static final List<Direction> HORIZON_DIRECTION = List.of(
            Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH
    );
    public static final List<MobEffect> HURT_EFFECTS = List.of(
            MobEffects.WITHER, MobEffects.POISON
    );
    public static final List<Item> NETHERITE_KITS = List.of(
            Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS
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

        // 下界合金套
        for (ItemStack itemStack : player.fabricPlayer.getInventory().armor) {
            if (NETHERITE_KITS.contains(itemStack.getItem()))
                return false;
        }

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

        try (Level level = CheatDetector.CLIENT.level) {
            if (level == null) return false;

            // hunger
            if (player.fabricPlayer.getFoodData().getFoodLevel() == 0 && level.getDifficulty() == Difficulty.HARD)
                return false;

            // MagmaBlock
            if (level.getBlockState(player.fabricPlayer.blockPosition().below()).getBlock() instanceof MagmaBlock)
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // as default
        return true;
    }

    public static boolean shouldBypassNextHurt(@NotNull TRPlayer player) {
        final Set<MobEffect> hasEffects = player.fabricPlayer.getActiveEffectsMap().keySet();

        return player.lastFallDistance > 3 && !hasEffects.contains(MobEffects.SLOW_FALLING);
    }
}
