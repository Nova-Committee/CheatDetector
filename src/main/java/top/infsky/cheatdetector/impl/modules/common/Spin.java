package top.infsky.cheatdetector.impl.modules.common;

import lombok.Getter;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.impl.utils.world.PlayerRotation;
import top.infsky.cheatdetector.config.*;

import java.util.HashSet;
import java.util.Set;

public class Spin extends Module {
    @Getter
    @Nullable
    private static Module instance = null;
    @Range(from = -180, to = 180)
    public float yaw;
    @Range(from = -90, to = 90)
    public float pitch;
    public boolean pitchReserve = false;

    private static final Set<Class<? extends Screen>> ALLOW_SCREENS = new HashSet<>(Set.of(
            ChatScreen.class,
            PauseScreen.class
    ));
    private int disableTicks = 0;
    public Spin(@NotNull TRSelf player) {
        super("Spin", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        // 😅这若智代码写了一天还没写出来
        // xRot竟然是pitch我难绷了
        // TODO move-fix
        if (isDisabled()) return;

        updateRot();

        if (disableTicks > 0) {
            disableTicks--;
            return;
        }

        if (Advanced3Config.spinAutoPause &&
                TRPlayer.CLIENT.screen != null && !ALLOW_SCREENS.contains(TRPlayer.CLIENT.screen.getClass())) {
            disableTicks = Advanced3Config.spinAutoPauseTime;
            return;
        }

        if (Advanced3Config.spinOnlyPacket) {
            PlayerRotation.silentRotate(yaw, pitch, player.currentOnGround);
        } else {
            PlayerRotation.rotate(yaw, pitch);
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

    @Override
    public boolean _onPacketSend(@NotNull Packet<?> basepacket, Connection connection, PacketSendListener listener, CallbackInfo ci) {
        if (isDisabled()) return false;

        if (basepacket instanceof ServerboundInteractPacket || basepacket instanceof ServerboundUseItemOnPacket) {
            PlayerRotation.silentRotate(player.fabricPlayer.getYRot(), player.fabricPlayer.getXRot(), player.fabricPlayer.onGround());
            disableTicks = Advanced3Config.spinAutoPauseTime;
            return false;
        }

        if (!Advanced3Config.spinOnlyPacket || disableTicks > 0) return false;
        if (basepacket instanceof ServerboundMovePlayerPacket packet)
            return PlayerRotation.cancelRotationPacket(packet, connection, listener, ci);
        return false;
    }

    @Override
    public void _onTeleport() {
        pitch = player.fabricPlayer.getXRot();
        yaw = player.fabricPlayer.getYRot();
    }
}
