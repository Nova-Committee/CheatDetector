package top.infsky.cheatdetector.impl.checks;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.config.AntiCheatConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.impl.utils.world.BlockUtils;
import top.infsky.cheatdetector.utils.TRPlayer;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MotionA extends Check {
    public static final List<MobEffect> IGNORED_EFFECTS = List.of(MobEffects.SLOW_FALLING);
    public static final List<Class<? extends Block>> IGNORED_BLOCKS = List.of(BedBlock.class, SlimeBlock.class, HoneyBlock.class, PowderSnowBlock.class);
    public static final List<Double> JUMP_MOTIONS = List.of(0.41159999516010254, -0.08506399504327788, -0.08336271676487925, -0.0816954640195993, -0.08006155629742463, -0.07846032669852913, -0.07689112166107052, -0.07535330069443089, -0.07384623611779237, -0.07236931280394177, -0.07092192792819801);
    public static final List<Double> JUMP_MOTIONS_1 = List.of(0.5095999912261959, -0.08702399309539816, -0.08528351489334113, -0.08357784622212827, -0.0819062908918066, -0.08026816663620898, -0.07866280483447859, -0.07708955023816295, -0.07554776070376615, -0.07403680693065001, -0.07255607220417704, -0.07110495214399076, -0.0696828544573303);
    public static final List<Double> JUMP_MOTIONS_2 = List.of(0.6076000164985658, -0.0889839917316434, -0.08720431359424546, -0.08546022898565087, -0.08375102603596235, -0.08207600711266715, -0.0804344885358894, -0.07882580029933772, -0.07724928579683382, -0.07570430155431032, -0.0741902169671691, -0.0727064140428918, -0.07125228714879878, -0.06982724276485225, -0.06843069924140427);

    private boolean readyToJump = false;
    private int disableTicks = 0;

    public MotionA(@NotNull TRPlayer player) {
        super("MotionA", player);
    }

    @Override
    public void _onTick() {
        if (disableTicks > 0) {
            disableTicks--;
            readyToJump = false;
            return;
        }

        if (!check()) {
            readyToJump = false;
            return;
        }

        if (player.currentOnGround && !readyToJump) {
            readyToJump = true;
        }

        if (!readyToJump) return;

        try {
            int tick = player.offGroundTicks - 1;
            if (tick >= 0) {
                List<Double> possibleMotion = Objects.requireNonNull(getPossibleMotions(player));

                if (Math.abs(player.currentMotion.y() - possibleMotion.get(tick)) > AntiCheatConfig.threshold) {
                    flag("Invalid jump motion at tick %s.".formatted(tick));
                }
            }
        } catch (IndexOutOfBoundsException ignored) {
            readyToJump = false;
        }
    }

    private boolean check() {
        if (player.fabricPlayer.isFallFlying()) {
            disableTicks = (int) Math.ceil(player.latency / 50.0) + 3;
            return false;
        }
        if (IGNORED_EFFECTS.stream().anyMatch(effect -> player.fabricPlayer.hasEffect(effect))
                || player.fabricPlayer.isInWall() || player.fabricPlayer.isInWater()
                || player.fabricPlayer.isPassenger() || player.fabricPlayer.isAutoSpinAttack()
                || player.fabricPlayer.isSwimming() || player.fabricPlayer.isSleeping()
                || player.fabricPlayer.onClimbable() || player.fabricPlayer.hurtTime > 0
                || !BlockUtils.isFullBlock(player.fabricPlayer.getBlockStateOn())
                || IGNORED_BLOCKS.stream().anyMatch(block -> block.isInstance(player.fabricPlayer.getBlockStateOn().getBlock()))
        ) return false;
        return !player.fabricPlayer.getAbilities().flying;
    }

    public static @Nullable List<Double> getPossibleMotions(@NotNull TRPlayer player) {
        List<Double> result;
        Map<MobEffect, MobEffectInstance> activeEffectsMap = player.fabricPlayer.getActiveEffectsMap();
        if (!activeEffectsMap.containsKey(MobEffects.JUMP)) {
            result = JUMP_MOTIONS;
        } else {
            switch (activeEffectsMap.get(MobEffects.JUMP).getAmplifier()) {
                case 0 -> result = JUMP_MOTIONS_1;
                case 1 -> result = JUMP_MOTIONS_2;
                default -> result = null;
            }
        }
        return result;
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.motionAAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.motionACheck;
    }
}
