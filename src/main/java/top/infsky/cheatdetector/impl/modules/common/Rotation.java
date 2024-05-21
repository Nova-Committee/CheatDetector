package top.infsky.cheatdetector.impl.modules.common;

import lombok.Getter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.compat.ReplaymodHelper;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.mixins.ConnectionAccessor;
import top.infsky.cheatdetector.utils.TRSelf;

import java.util.Objects;

import static net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.*;

public class Rotation extends Module {
    @Getter
    @Nullable
    private static Module instance = null;

    private Packet<?> sending = null;

    @Nullable
    private static Float yaw = null;
    @Nullable
    private static Float pitch = null;

    private boolean stoppedSprint = false;
    private long lastStopSprintTime = -1;

    public Rotation(@NotNull TRSelf player) {
        super("Rotation", player);
        instance = this;
    }

    public static void silentRotate(float yaw, float pitch) {
        Rotation.yaw = yaw;
        Rotation.pitch = pitch;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) {
            yaw = null;
            pitch = null;
        }
    }

    public void onFinallyPacketSend(ConnectionAccessor connection, Packet<?> basePacket, @Nullable PacketSendListener packetSendListener, ConnectionProtocol connectionProtocol, ConnectionProtocol connectionProtocol2, CallbackInfo ci) {
        if (isDisabled()) return;
        if (FabricLoader.getInstance().isModLoaded("replaymod")) {
            if (ReplaymodHelper.isFromReplayMod(packetSendListener)) return;
        }

        if (sending == basePacket) {
            sending = null;
            return;
        }

        if (basePacket instanceof ServerboundMovePlayerPacket packet) {
            ci.cancel();

            if (Advanced3Config.rotationFixSprintVulcan && yaw != null && pitch != null) {
                if (!stoppedSprint && player.upTime - lastStopSprintTime > Advanced3Config.rotationFixSprintDelay) {
                    player.fabricPlayer.connection.send(new ServerboundPlayerCommandPacket(player.fabricPlayer, ServerboundPlayerCommandPacket.Action.STOP_SPRINTING));
                    stoppedSprint = true;
                    lastStopSprintTime = player.upTime;
                }
            } else if (stoppedSprint) {
                player.fabricPlayer.connection.send(new ServerboundPlayerCommandPacket(player.fabricPlayer, ServerboundPlayerCommandPacket.Action.START_SPRINTING));
            }

            if (basePacket instanceof Pos) {
                send(connection, new Pos(packet.getX(player.fabricPlayer.getX()), packet.getY(player.fabricPlayer.getY()), packet.getZ(player.fabricPlayer.getZ()), packet.isOnGround()), packetSendListener, connectionProtocol, connectionProtocol2);
            } else if (basePacket instanceof Rot) {
                send(connection, new Rot(getYaw(packet), getPitch(packet), packet.isOnGround()), packetSendListener, connectionProtocol, connectionProtocol2);
            } else if (basePacket instanceof PosRot) {
                send(connection, new PosRot(packet.getX(player.fabricPlayer.getX()), packet.getY(player.fabricPlayer.getY()), packet.getZ(player.fabricPlayer.getZ()), getYaw(packet), getPitch(packet), packet.isOnGround()), packetSendListener, connectionProtocol, connectionProtocol2);
            } else if (basePacket instanceof StatusOnly) {
                send(connection, new StatusOnly(packet.isOnGround()), packetSendListener, connectionProtocol, connectionProtocol2);
            }
        }
        if (basePacket instanceof ServerboundPlayerCommandPacket packet) {
            if (Advanced3Config.rotationFixSprintVulcan && stoppedSprint) {
                if (packet.getAction() == ServerboundPlayerCommandPacket.Action.START_SPRINTING) {
                    ci.cancel();
                }
            }
        }
    }

    private float getYaw(@NotNull ServerboundMovePlayerPacket packet) {
        float result = Objects.requireNonNullElse(yaw, packet.getYRot(player.fabricPlayer.getYRot()));
        yaw = null;
        return result;
    }

    private float getPitch(@NotNull ServerboundMovePlayerPacket packet) {
        float result = Objects.requireNonNullElse(pitch, packet.getXRot(player.fabricPlayer.getXRot()));
        pitch = null;
        return result;
    }

    private void send(@NotNull ConnectionAccessor connection, Packet<?> packet, @Nullable PacketSendListener packetSendListener, ConnectionProtocol connectionProtocol, ConnectionProtocol connectionProtocol2) {
        sending = packet;
        connection.doSendPacket(packet, packetSendListener, connectionProtocol, connectionProtocol2);
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.rotationEnabled;
    }
}
