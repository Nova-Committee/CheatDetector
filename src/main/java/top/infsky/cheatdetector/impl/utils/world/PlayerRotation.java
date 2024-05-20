package top.infsky.cheatdetector.impl.utils.world;

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
    public static float getYaw(@NotNull Vec3 pos) {
        LocalPlayer player = TRSelf.getInstance().fabricPlayer;
        return player.getYRot() + Mth.wrapDegrees((float) Math.toDegrees(Math.atan2(pos.z() - player.getZ(), pos.x() - player.getX())) - 90f - player.getYRot());
    }

    public static float getPitch(@NotNull BlockPos pos) {
        return getPitch(new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
    }
    public static float getPitch(@NotNull Vec3 pos) {
        LocalPlayer player = TRSelf.getInstance().fabricPlayer;

        double diffX = pos.x() - player.getX();
        double diffY = pos.y() - (player.getY() + player.getEyeHeight(player.getPose()));
        double diffZ = pos.z() - player.getZ();

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        return player.getXRot() + Mth.wrapDegrees((float) -Math.toDegrees(Math.atan2(diffY, diffXZ)) - player.getXRot());
    }

    public static void rotate(double yaw, double pitch) {
        EntityAccessor camera = (EntityAccessor) TRSelf.getInstance().fabricPlayer;

        camera.doSetXRot((float) pitch);
        camera.doSetYRot((float) yaw);
    }
}
