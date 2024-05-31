package top.infsky.cheatdetector.impl.fixes;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundPongPacket;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.compat.MinihudHelper;
import top.infsky.cheatdetector.config.Advanced2Config;
import top.infsky.cheatdetector.config.AntiCheatConfig;
import top.infsky.cheatdetector.impl.Fix;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.utils.TRSelf;

public class ServerFreeze extends Fix {
    private long lastReceiveTime = Long.MAX_VALUE;
    private int pingRequest = 0;
    @Getter
    private static FreezeType freezeType = FreezeType.NONE;

    public ServerFreeze(@NotNull TRSelf player) {
        super("ServerFreeze", player);
    }

    @Override
    public void _onTick() {
        if (isDisabled()) {
            return;
        }

        long current = System.currentTimeMillis();
        if (current - lastReceiveTime > Advanced2Config.serverFreezeMaxMs) {
            onFreeze(current, FreezeType.NO_PACKET_RECEIVE);
        }
        if (FabricLoader.getInstance().isModLoaded("minihud") && MinihudHelper.getServerTPS() < Advanced2Config.serverFreezeMinTPS) {
            onFreeze(current, FreezeType.LOW_TPS);
        } else if (freezeType == FreezeType.LOW_TPS) {
            freezeType = FreezeType.NONE;
        }

        if (Advanced2Config.serverFreezePostDelay != -1 && player.upTime % Advanced2Config.serverFreezePostDelay == 0) {
            player.fabricPlayer.connection.send(new ServerboundPongPacket(pingRequest));
            pingRequest = pingRequest < -32767 ? 0 : pingRequest - 1;
        }
    }

    @Override
    public boolean _onPacketReceive(@NotNull Packet<ClientGamePacketListener> basePacket, Connection connection, ChannelHandlerContext channelHandlerContext, CallbackInfo ci) {
        long current = System.currentTimeMillis();
        if (freezeType == FreezeType.NO_PACKET_RECEIVE) {
            freezeType = FreezeType.NONE;
        }
        if (current - lastReceiveTime > Advanced2Config.serverFreezeMaxMs) {
            customMsg(Component.translatable("cheatdetector.chat.alert.freezeDetected").getString()
                    + ChatFormatting.DARK_RED + (current - lastReceiveTime) + "ms");
        }

        lastReceiveTime = current;
        return false;
    }

    private void onFreeze(long currentTime, FreezeType reason) {
        if (reason == FreezeType.LOW_TPS && freezeType == FreezeType.NONE && Advanced2Config.serverFreezeAlertTPS) {
            customMsg((Component.translatable("cheatdetector.chat.alert.lowTPS").getString()
                    + ChatFormatting.DARK_RED + "%.2f").formatted(MinihudHelper.getServerTPS()));
        }
        if (Advanced2Config.serverFreezeAlert && reason == FreezeType.NO_PACKET_RECEIVE) {
            TRPlayer.CLIENT.gui.setOverlayMessage(
                    Component.literal(Component.translatable("cheatdetector.overlay.alert.serverFreezeAlert")
                            .withStyle(ChatFormatting.DARK_RED)
                            .getString()
                            .formatted(currentTime - lastReceiveTime)),
                    false);
        }

        freezeType = reason;
        if (Advanced2Config.serverFreezeAutoDisableCheck)
            CheatDetector.manager.getDataMap().forEach((uuid, player1) -> player1.manager.disableTick = 10);
    }

    @Override
    public int getAlertBuffer() {
        return Advanced2Config.serverFreezeAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !Advanced2Config.serverFreezeEnabled|| !AntiCheatConfig.falseFlagFix;
    }

    public enum FreezeType {
        NONE,
        NO_PACKET_RECEIVE,
        LOW_TPS
    }
}
