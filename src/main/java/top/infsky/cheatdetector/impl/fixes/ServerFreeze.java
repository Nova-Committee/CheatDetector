package top.infsky.cheatdetector.impl.fixes;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.common.ServerboundPongPacket;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.config.Advanced2Config;
import top.infsky.cheatdetector.config.AntiCheatConfig;
import top.infsky.cheatdetector.impl.Fix;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.utils.TRSelf;

public class ServerFreeze extends Fix {
    private long lastReceiveTime = Long.MAX_VALUE;
    private int pingRequest = 0;

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
            if (Advanced2Config.serverFreezeAutoDisableCheck)
                CheatDetector.manager.getDataMap().forEach((uuid, player1) -> player1.manager.disableTick = 10);
            if (Advanced2Config.serverFreezeAlert) {
                TRPlayer.CLIENT.gui.setOverlayMessage(
                        Component.literal(Component.translatable("cheatdetector.overlay.alert.serverFreezeAlert")
                                .withStyle(ChatFormatting.DARK_RED)
                                .getString()
                                .formatted(current - lastReceiveTime)),
                        false);
            }
        }

        if (Advanced2Config.serverFreezePostDelay != -1 && player.upTime % Advanced2Config.serverFreezePostDelay == 0) {
            player.fabricPlayer.connection.send(new ServerboundPongPacket(pingRequest));
            pingRequest = pingRequest < -32767 ? 0 : pingRequest - 1;
        }
    }

    @Override
    public boolean _onPacketReceive(@NotNull Packet<ClientGamePacketListener> basePacket, Connection connection, ChannelHandlerContext channelHandlerContext, CallbackInfo ci) {
        long current = System.currentTimeMillis();
        if (current - lastReceiveTime > Advanced2Config.serverFreezeMaxMs) {
            customMsg(Component.translatable("cheatdetector.chat.alert.freezeDetected").getString()
                    + ChatFormatting.DARK_RED + (current - lastReceiveTime) + "ms");
        }

        lastReceiveTime = current;
        return false;
    }

    @Override
    public int getAlertBuffer() {
        return Advanced2Config.serverFreezeAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !Advanced2Config.serverFreezeEnabled|| !AntiCheatConfig.falseFlagFix;
    }
}
