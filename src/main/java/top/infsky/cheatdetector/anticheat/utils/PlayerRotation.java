package top.infsky.cheatdetector.anticheat.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

public class PlayerRotation {
    /**
     * 无法使用
     */
    @SuppressWarnings("unused")
    public static @NotNull Pair<Float, Float> getLookAt(@NotNull ServerPlayer player, @NotNull BlockPos blockPos) {
        double dx = (blockPos.getX() + 0.5) - player.getX();
        double dy = (blockPos.getY() + 0.5) - (player.getY() + player.getEyeHeight());
        double dz = (blockPos.getZ() + 0.5) - player.getZ();
        double dh = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90;
        float pitch = (float) -Math.toDegrees(Math.atan2(dy, dh));
        return new Pair<>(yaw, pitch);
    }
}
