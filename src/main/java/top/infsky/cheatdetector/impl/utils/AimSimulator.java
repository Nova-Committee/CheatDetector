package top.infsky.cheatdetector.impl.utils;

import net.minecraft.world.entity.Entity;
import oshi.util.tuples.Pair;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.impl.utils.world.PlayerRotation;
import top.infsky.cheatdetector.utils.TRPlayer;

public class AimSimulator {
    @Contract("_, _ -> new")
    public static @NotNull Pair<Double, Double> getLegitAim(@NotNull Entity target, @NotNull TRPlayer player) {
        Vec3 aimPos;

        double yDiff = target.position().y() - player.currentPos.y();
        if (yDiff >= 0) {
            if (target.getEyePosition().y() - yDiff > target.position().y()) {
                aimPos = new Vec3(target.getEyePosition().x(), target.getEyePosition().y() - yDiff, target.getEyePosition().z());
            } else {
                aimPos = new Vec3(target.position().x(), target.position().y() + 0.4, target.position().z());
            }
        } else {
            aimPos = target.getEyePosition();
        }

        aimPos = getRandomAimPos(aimPos);

        return new Pair<>(PlayerRotation.getYaw(aimPos), PlayerRotation.getPitch(aimPos));
    }

    @Contract("_ -> new")
    private static @NotNull Vec3 getRandomAimPos(@NotNull Vec3 basicAimPos) {
        return new Vec3(
                basicAimPos.x() + (Math.random() - 0.5) * 2 * 0.35,
                basicAimPos.y() + (Math.random() - 0.5) * 2 * 0.45,
                basicAimPos.z() + (Math.random() - 0.5) * 2 * 0.35
                );
    }
}
