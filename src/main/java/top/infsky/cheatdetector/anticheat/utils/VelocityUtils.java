package top.infsky.cheatdetector.anticheat.utils;

import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.MagmaBlock;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.anticheat.TRPlayer;

import java.util.List;
import java.util.Set;

public class VelocityUtils {
    public static final List<MobEffect> hurtEffects = List.of(
            MobEffects.WITHER,
            MobEffects.POISON
    );
    public static boolean shouldCheck(@NotNull TRPlayer player) {
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

        // effect
        for (MobEffect effect : hurtEffects) {
            if (hasEffects.contains(effect))
                return false;
        }

        return true;
    }
}
