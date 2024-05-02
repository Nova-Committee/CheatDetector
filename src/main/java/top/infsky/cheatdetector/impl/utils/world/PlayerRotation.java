package top.infsky.cheatdetector.impl.utils.world;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.mixins.ConnectionInvoker;
import top.infsky.cheatdetector.mixins.EntityInvoker;

import java.util.Objects;

public class PlayerRotation {
    public static double getYaw(@NotNull BlockPos pos) {
        return getYaw(new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
    }
    public static double getYaw(@NotNull Vec3 pos) {
        LocalPlayer player = TRSelf.getInstance().fabricPlayer;
        return player.getYRot() + Mth.wrapDegrees((float) Math.toDegrees(Math.atan2(pos.z() - player.getZ(), pos.x() - player.getX())) - 90f - player.getYRot());
    }

    public static double getPitch(@NotNull BlockPos pos) {
        return getPitch(new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
    }
    public static double getPitch(@NotNull Vec3 pos) {
        LocalPlayer player = TRSelf.getInstance().fabricPlayer;

        double diffX = pos.x() - player.getX();
        double diffY = pos.y() - (player.getY() + player.getEyeHeight(player.getPose()));
        double diffZ = pos.z() - player.getZ();

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        return player.getXRot() + Mth.wrapDegrees((float) -Math.toDegrees(Math.atan2(diffY, diffXZ)) - player.getXRot());
    }

    public static void silentRotate(double yaw, double pitch, boolean onGround) {
        Connection connection = Objects.requireNonNull(TRPlayer.CLIENT.getConnection()).getConnection();

        ((ConnectionInvoker) connection).sendPacket(new ServerboundMovePlayerPacket.Rot((float) yaw, (float) pitch, onGround), null);
    }

    public static void rotate(double yaw, double pitch) {
        EntityInvoker camera = (EntityInvoker) TRSelf.getInstance().fabricPlayer;

        camera.doSetXRot((float) pitch);
        camera.doSetYRot((float) yaw);
    }

    public static boolean cancelRotationPacket(@NotNull ServerboundMovePlayerPacket packet, @Nullable Connection connection, @Nullable PacketSendListener listener, @Nullable CallbackInfo ci) {
        if (ci != null && ci.isCancelled()) return false;
        if (!CheatDetector.inWorld) {
            if (ci != null) ci.cancel();
            return true;
        }

        if (packet.hasRotation()) {
            if (ci != null) ci.cancel();
            if (packet.hasPosition()) {  // PosRot
                if (connection != null)
                    ((ConnectionInvoker) connection).sendPacket(
                            new ServerboundMovePlayerPacket.Pos(packet.getX(0), packet.getY(0), packet.getZ(0), packet.isOnGround()), listener
                    );
            }
            return true;
        }
        return false;
    }
}
