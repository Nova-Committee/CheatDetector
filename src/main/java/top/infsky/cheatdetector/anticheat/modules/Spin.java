package top.infsky.cheatdetector.anticheat.modules;

import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.anticheat.Module;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.anticheat.TRSelf;
import top.infsky.cheatdetector.config.*;
import top.infsky.cheatdetector.mixins.MixinEntity;

public class Spin extends Module {
    @Range(from = -180, to = 180)
    public float yaw;
    @Range(from = -90, to = 90)
    public float pitch;
    public boolean pitchReserve = false;
    @Nullable
    public ServerboundMovePlayerPacket tickUnSend = null;

    private boolean sending = false;

    public Spin(@NotNull TRSelf player) {
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

        if (Advanced3Config.spinOnlyPacket) {
            packetRot();
        } else {
            camera.doSetXRot(pitch);
            camera.doSetYRot(yaw);
        }
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.spinEnabled;
    }

    public void updateRot() {
        check();
        if (Advanced3Config.spinDoSpinYaw)
            yaw += (float) Advanced3Config.spinYawStep;
        else
            yaw = (float) Advanced3Config.spinDefaultYaw;
        if (Advanced3Config.spinDoSpinPitch)
            pitch += (float) (Advanced3Config.spinPitchStep * (pitchReserve ? -1 : 1));
        else
            pitch = (float) Advanced3Config.spinDefaultPitch;
        check();
    }

    public void check() {
        // pitch
        if (Advanced3Config.spinAllowBadPitch) {
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
        if (!CheatDetector.inWorld) return;

        if (tickUnSend != null && (FixesConfig.packetFixEnabled && FixesConfig.getPacketFixMode() == Fixes.STRICT)) {
            // 防止丢失包，即使有被检测的风险
            sending = true;
            TRPlayer.CLIENT.getConnection().send(tickUnSend);
            sending = false;
            tickUnSend = null;
        }
        tickUnSend = new ServerboundMovePlayerPacket.Rot(yaw, pitch, player.fabricPlayer.onGround());
    }

    @Override
    public boolean _handleMovePlayer(@NotNull ServerboundMovePlayerPacket packet, @NotNull Connection connection, PacketSendListener listener, @NotNull CallbackInfo ci) {
        if (ci.isCancelled()) return false;
        if (!CheatDetector.inWorld) { ci.cancel(); return true; }
        if (isDisabled()) return false;
        if (!Advanced3Config.spinOnlyPacket) return false;

        if (!sending && packet.hasRotation()) {
            ci.cancel();
            if (packet.hasPosition()) {  // PosRot
                connection.send(
                        new ServerboundMovePlayerPacket.Pos(packet.getX(0), packet.getY(0), packet.getZ(0), packet.isOnGround())
                        , listener
                );
                tickUnSend = null;
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
