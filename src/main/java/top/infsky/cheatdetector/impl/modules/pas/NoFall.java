package top.infsky.cheatdetector.impl.modules.pas;

import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.mixins.ConnectionAccessor;
import top.infsky.cheatdetector.utils.TRSelf;

import java.util.List;

public class NoFall extends Module {
    @Getter
    @Nullable
    private static Module instance = null;
    public static final List<Integer> maxModCount = List.of(4, 4, 3);
    private int currentModCount = 0;
    private int hasModCount = 0;  // 执行NoFall的次数

    public NoFall(@NotNull TRSelf player) {
        super("NoFall", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        if (player.currentOnGround && currentModCount > 0) {
            hasModCount++;
            currentModCount = 0;
        }
    }

    @Override
    public boolean _onPacketSend(@NotNull Packet<ServerGamePacketListener> basePacket, Connection connection, PacketSendListener listener, CallbackInfo ci) {
        if (isDisabled()) return false;

        if (basePacket instanceof ServerboundMovePlayerPacket packet && player.fabricPlayer.fallDistance > 7.0) {
            customMsg("currentMod: %s  hasMod: %s".formatted(currentModCount, hasModCount));
            if (currentModCount > maxModCount.get(hasModCount % maxModCount.size())) {
                return false;
            }

            ci.cancel();
            player.fabricPlayer.resetFallDistance();
            player.fabricPlayer.setDeltaMovement(0, 0, 0);
            if (packet instanceof ServerboundMovePlayerPacket.PosRot) {
                ((ConnectionAccessor) connection).sendPacket(
                        new ServerboundMovePlayerPacket.PosRot(packet.getX(0), packet.getY(0), packet.getZ(0), packet.getYRot(0), packet.getXRot(0), true),
                        listener);
            } else if (packet instanceof ServerboundMovePlayerPacket.Pos) {
                ((ConnectionAccessor) connection).sendPacket(
                        new ServerboundMovePlayerPacket.Pos(packet.getX(0), packet.getY(0), packet.getZ(0), true),
                        listener);
            } else if (packet instanceof ServerboundMovePlayerPacket.Rot) {
                ((ConnectionAccessor) connection).sendPacket(
                        new ServerboundMovePlayerPacket.Rot(packet.getYRot(0), packet.getXRot(0), true),
                        listener);
            } else if (packet instanceof ServerboundMovePlayerPacket.StatusOnly) {
                ((ConnectionAccessor) connection).sendPacket(
                        new ServerboundMovePlayerPacket.StatusOnly(true),
                        listener);
            }
            currentModCount++;
            return true;
        }
        return false;
    }

    @Override
    public boolean isDisabled() {
        if (!ModuleConfig.noFallEnabled) return true;
        if (ModuleConfig.antiFallEnabled) {
            customMsg(Component.translatable("cheatdetector.chat.alert.couldNotWorkWith").withStyle(ChatFormatting.DARK_RED).getString().formatted(
                    Component.translatable("cheatdetector.config.modules.noFallEnabled.pretty_name").getString(),
                    Component.translatable("cheatdetector.config.modules.antiFallEnabled.pretty_name").getString()
            ));
            CheatDetector.CONFIG_HANDLER.configManager.setValue("noFallEnabled", false);
            return true;
        }
        return !ModuleConfig.aaaPASModeEnabled;
    }
}
