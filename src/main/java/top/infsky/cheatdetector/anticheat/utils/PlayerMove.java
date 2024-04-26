package top.infsky.cheatdetector.anticheat.utils;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PlayerMove {
    public static double getXzTickSpeed(@NotNull Vec3 lastTick, @NotNull Vec3 currentTick) {
            return getXZOnlyPos(currentTick).distanceTo(getXZOnlyPos(lastTick));
    }

    public static double getXzSecSpeed(@NotNull Vec3 lastTick, @NotNull Vec3 currentTick) {
        return getXzTickSpeed(lastTick, currentTick) * 20;  // IDK what's the fucking tps
    }

    @Contract("_ -> new")
    public static @NotNull Vec3 getXZOnlyPos(@NotNull Vec3 position) {
        return new Vec3(position.x(), 0, position.z());
    }

    @Contract("_ -> new")
    public static @NotNull Vec3 getYOnlyPos(@NotNull Vec3 position) {
        return new Vec3(0, position.y(), 0);
    }

    public static double getJumpDistance(@NotNull AbstractClientPlayer player) {
        try {
            final int x = Objects.requireNonNull(player.getActiveEffectsMap().get(MobEffects.JUMP)).getAmplifier() + 1;
//            return -9.331952072919326 * x * x - 3.672263213983712 * x + 0.6261016701268645;  // chat gpt
          return 0.04837 * x + 0.5356 * x + 1.252;  // numpy
        } catch (NullPointerException e) {
            return 1.252203340253729;
        }
    }
}
