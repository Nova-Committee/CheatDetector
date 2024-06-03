package top.infsky.cheatdetector.impl.utils.world;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class RayCastUtils {
    public static @NotNull BlockHitResult blockRayCast(@NotNull Player player, @NotNull BlockGetter level, double reach) {
        Vec3 vec3 = player.getEyePosition(0);
        Vec3 vec32 = player.getViewVector(0);
        Vec3 vec33 = vec3.add(vec32.x * reach, vec32.y * reach, vec32.z * reach);
        return level.isBlockInLine(new ClipBlockStateContext(vec3, vec33, blockState -> !blockState.isAir()));
    }
}
