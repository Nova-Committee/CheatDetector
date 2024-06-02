package top.infsky.cheatdetector.impl.utils.world;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.mixins.EntityAccessor;

public class PlayerRotation {
    public static float getYaw(@NotNull BlockPos pos) {
        return getYaw(new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
    }
    public static float getYaw(@NotNull AbstractClientPlayer from, @NotNull Vec3 pos) {
        return from.getYRot() + Mth.wrapDegrees((float) Math.toDegrees(Math.atan2(pos.z() - from.getZ(), pos.x() - from.getX())) - 90f - from.getYRot());
    }

    public static float getYaw(@NotNull Vec3 pos) {
        return getYaw(TRSelf.getInstance().fabricPlayer, pos);
    }

    public static float getPitch(@NotNull BlockPos pos) {
        return getPitch(new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
    }

    public static float getPitch(@NotNull AbstractClientPlayer from, @NotNull Vec3 pos) {
        double diffX = pos.x() - from.getX();
        double diffY = pos.y() - (from.getY() + from.getEyeHeight(from.getPose()));
        double diffZ = pos.z() - from.getZ();

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        return from.getXRot() + Mth.wrapDegrees((float) -Math.toDegrees(Math.atan2(diffY, diffXZ)) - from.getXRot());
    }

    public static float getPitch(@NotNull Vec3 pos) {
        return getPitch(TRSelf.getInstance().fabricPlayer, pos);
    }

    public static void rotate(double yaw, double pitch) {
        EntityAccessor camera = (EntityAccessor) TRSelf.getInstance().fabricPlayer;

        camera.doSetXRot((float) pitch);
        camera.doSetYRot((float) yaw);
    }
}
