package top.infsky.cheatdetector.impl.utils;

import oshi.util.tuples.Pair;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.impl.utils.world.PlayerRotation;
import top.infsky.cheatdetector.utils.TRPlayer;

public class AimSimulator {
    @Contract("_, _ -> new")
    public static @NotNull Pair<Double, Double> getLegitAim(@NotNull AbstractClientPlayer target, @NotNull TRPlayer player) {
        double yaw, pitch;

        double yDiff = target.position().y() - player.currentPos.y();
        if (yDiff >= 0) {
            if (target.getEyePosition().y() - yDiff > target.position().y()) {
                pitch = PlayerRotation.getPitch(new Vec3(target.getEyePosition().x(), target.getEyePosition().y() - yDiff, target.getEyePosition().z()));
            } else {
                pitch = PlayerRotation.getPitch(new Vec3(target.position().x(), target.position().y() + 0.4, target.position().z()));
            }
        } else {
            pitch = PlayerRotation.getPitch(target.getEyePosition());
        }

        yaw = PlayerRotation.getYaw(target.position());

        pitch += (Math.random() - 0.5) * 2 * 0.4;
        yaw += (Math.random() - 0.5) * 2 * 0.6;

        return new Pair<>(yaw, pitch);
    }
}
