package top.infsky.cheatdetector.anticheat.utils;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.anticheat.TRSelf;

import java.util.Objects;

public class PlayerRotation {
    public static double getYaw(@NotNull BlockPos pos) {
        LocalPlayer player = TRSelf.getInstance().fabricPlayer;
        return player.getYRot() + Mth.wrapDegrees((float) Math.toDegrees(Math.atan2(pos.getZ() + 0.5 - player.getZ(), pos.getX() + 0.5 - player.getX())) - 90f - player.getYRot());
    }

    public static double getPitch(BlockPos pos) {
        LocalPlayer player = TRSelf.getInstance().fabricPlayer;

        double diffX = pos.getX() + 0.5 - player.getX();
        double diffY = pos.getY() + 0.5 - (player.getY() + player.getEyeHeight(player.getPose()));
        double diffZ = pos.getZ() + 0.5 - player.getZ();

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        return player.getXRot() + Mth.wrapDegrees((float) -Math.toDegrees(Math.atan2(diffY, diffXZ)) - player.getXRot());
    }

    public static void silentRotate(double yaw, double pitch, boolean onGround) {
        Connection connection = Objects.requireNonNull(TRPlayer.CLIENT.getConnection()).getConnection();

        connection.send(new ServerboundMovePlayerPacket.Rot((float) yaw, (float) pitch, onGround));
    }
}
