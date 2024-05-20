package top.infsky.cheatdetector.impl.fixes;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundPongPacket;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.config.Advanced2Config;
import top.infsky.cheatdetector.config.AntiCheatConfig;
import top.infsky.cheatdetector.impl.Fix;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.utils.TRSelf;

public class ServerFreeze extends Fix {
    private long lastReceiveTime = -1;
    private int pingRequest = 0;

    public ServerFreeze(@NotNull TRSelf player) {
        super("ServerFreeze", player);
    }

    @Override
    public void _onTick() {
        if (isDisabled()) {
            return;
        }

        if (player.upTime - lastReceiveTime > Advanced2Config.serverFreezeMaxTicks) {
            if (Advanced2Config.serverFreezeAutoDisableCheck)
                CheatDetector.manager.getDataMap().forEach((uuid, player1) -> player1.manager.disableTick = 10);
            if (Advanced2Config.serverFreezeAlert) {
                TRPlayer.CLIENT.gui.setOverlayMessage(
                        Component.literal(Component.translatable("cheatdetector.overlay.alert.serverFreezeAlert")
                                .withStyle(ChatFormatting.DARK_RED)
                                .getString()
                                .formatted((player.upTime - lastReceiveTime) * 50)),
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
        lastReceiveTime = player.upTime;
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
