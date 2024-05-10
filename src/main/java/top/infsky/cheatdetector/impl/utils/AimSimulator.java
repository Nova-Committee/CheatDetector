package top.infsky.cheatdetector.impl.utils;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Triplet;
import top.infsky.cheatdetector.impl.utils.world.PlayerRotation;
import top.infsky.cheatdetector.utils.TRSelf;

public class AimSimulator {
    public static @NotNull Pair<Double, Double> getLegitAim(@NotNull Entity target, @NotNull TRSelf player,
                                                            @Nullable Pair<Double, Double> speed, @Nullable Triplet<Double, Double, Double> offset,
                                                            boolean noise1, Pair<Double, Double> noise1Random,
                                                            boolean noise2, Pair<Double, Double> noise2Random) {
        double yaw, pitch;

        double yDiff = target.position().y() - player.currentPos.y();
        Vec3 targetPosition;
        if (yDiff >= 0) {
            if (target.getEyePosition().y() - yDiff > target.position().y()) {
                targetPosition = new Vec3(target.getEyePosition().x(), target.getEyePosition().y() - yDiff, target.getEyePosition().z());
            } else {
                targetPosition = new Vec3(target.position().x(), target.position().y() + 0.4, target.position().z());
            }
        } else {
            targetPosition = target.getEyePosition();
        }

        if (offset != null) {
            targetPosition = targetPosition.add(offset.getA(), offset.getB(), offset.getC());
        }

        if (noise2) {
            targetPosition = targetPosition.add(random(noise2Random.getA()), random(noise2Random.getB()), random(noise2Random.getA()));
        }

        yaw = PlayerRotation.getYaw(targetPosition);
        pitch = PlayerRotation.getPitch(targetPosition);

        if (noise1) {
            yaw += random(noise1Random.getA());
            pitch += random(noise1Random.getB());
        }

        if (speed != null) {
            yaw = rotMove(yaw, player.fabricPlayer.getYRot(), speed.getA());
            pitch = rotMove(pitch, player.fabricPlayer.getXRot(), speed.getB());
        }

        return new Pair<>(yaw, pitch);
    }

    private static double random(double multiple) {
        return (Math.random() - 0.5) * 2 * multiple;
    }

    private static double rotMove(double target, double current, double diff) {
        if (target > current)
            if (target - current > diff)
                return current + diff;
            else
                return target;
        else
            if (current - target > diff)
                return current - diff;
            else
                return target;
    }
}
