package top.infsky.cheatdetector.anticheat.utils;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class PlayerMove {
    public static double getXzTickSpeed(@NotNull Vec3 lastTick, @NotNull Vec3 currentTick) {
        Vec3 prefixLast = new Vec3(lastTick.x(), 0, lastTick.z());
        Vec3 prefixCurrent = new Vec3(currentTick.x(), 0, currentTick.z());
        return prefixCurrent.distanceTo(prefixLast);
    }

    public static double getXzSecSpeed(@NotNull Vec3 lastTick, @NotNull Vec3 currentTick) {
        return getXzTickSpeed(lastTick, currentTick) * 20;
    }
}
