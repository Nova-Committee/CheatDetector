package top.infsky.cheatdetector.anticheat.fixs;

import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.anticheat.Fix;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.mixins.MixinEntity;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

public class Spin extends Fix {
    @Range(from = -180, to = 180)
    public float yaw;
    @Range(from = -90, to = 90)
    public float pitch;
    public boolean pitchReserve = false;

    public Spin(@NotNull TRPlayer player) {
        super("Spin", player);
    }

    @Override
    public void _onTick() {
        // 😅这若智代码写了一天还没写出来
        // xRot竟然是pitch我难绷了
        // TODO 静默转头明天再说
        // TODO move-fix
        if (isDisabled()) return;

        final MixinEntity camera = (MixinEntity) player.fabricPlayer;
        if (camera == null) return;

        updateRot();

        if (CONFIG().getAdvanced2().isSpinOnlyPacket()) {
            packetRot();
        } else {
            camera.doSetXRot(pitch);
            camera.doSetYRot(yaw);
        }
    }

    @Override
    public boolean isDisabled() {
        return !CONFIG().getFixes().isSpinEnabled();
    }

    public void updateRot() {
        check();
        if (CONFIG().getAdvanced2().isSpinDoSpinYaw())
            yaw += CONFIG().getAdvanced2().getSpinYawStep();
        else
            yaw = CONFIG().getAdvanced2().getSpinDefaultYaw();
        if (CONFIG().getAdvanced2().isSpinDoSpinPitch())
            pitch += CONFIG().getAdvanced2().getSpinPitchStep() * (pitchReserve ? -1 : 1);
        else
            pitch = CONFIG().getAdvanced2().getSpinDefaultPitch();
        check();
    }

    public void check() {
        // pitch
        if (CONFIG().getAdvanced2().isSpinAllowBadPitch()) {
            pitchReserve = false;
            // blatant转头
            if (pitch >= 180) {  // 向下转了一圈
                pitch = -180;
            } else if (yaw <= -180) {  // 向上转了一圈
                pitch = 180;
            }
        } else {
            // legit转头
            if (pitch >= 90) {  // 低头太低
                pitch = 90;
                pitchReserve = true;
            } else if (pitch <= -90) {  // 抬头太高
                pitch = -90;
                pitchReserve = false;
            }
        }
    }

    public void packetRot() {
        if (TRPlayer.CLIENT.getConnection() == null) return;

        TRPlayer.CLIENT.getConnection().send(
                new ServerboundMovePlayerPacket.Rot(yaw, pitch, player.fabricPlayer.onGround())
        );
    }

    @Override
    public boolean _handleMovePlayer(ServerboundMovePlayerPacket packet, Connection connection, PacketSendListener listener, CallbackInfo ci) {
        if (isDisabled()) return false;
        if (!CONFIG().getAdvanced2().isSpinOnlyPacket()) return false;

        if (packet.getXRot(pitch) != pitch || packet.getYRot(yaw) != yaw) {
            ci.cancel();
            if (packet.hasPosition()) {
                connection.send(
                        new ServerboundMovePlayerPacket.PosRot(packet.getX(0), packet.getY(0), packet.getZ(0), yaw, pitch, packet.isOnGround()),
                        listener
                );
            }
        }
        return false;
    }

    @Override
    public void _onTeleport() {
        pitch = player.fabricPlayer.getXRot();
        yaw = player.fabricPlayer.getYRot();
    }
}
