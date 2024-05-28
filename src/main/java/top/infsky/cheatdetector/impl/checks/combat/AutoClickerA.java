package top.infsky.cheatdetector.impl.checks.combat;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.config.AntiCheatConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.utils.TRPlayer;

public class AutoClickerA extends Check {
    private long click1 = -1;
    private long click2 = -1;
    private long click3 = -1;
    public AutoClickerA(@NotNull TRPlayer player) {
        super("AutoClickerA", player);
    }

    @Override
    public void _onTick() {
        if (click2 == -1 || click1 == -1) return;

        long delay1 = Math.abs(click2 - click1);
        long delay2 = Math.abs(click3 - click2);

        if (Math.abs(delay1 - delay2) < AdvancedConfig.autoClickerAMinDiffMs) {
            flag("constant click delay: %s->%s".formatted(delay2, delay1));
        }
    }

    @Override
    public boolean _onPacketReceive(@NotNull Packet<ClientGamePacketListener> basePacket, Connection connection, ChannelHandlerContext channelHandlerContext, CallbackInfo ci) {
        if (basePacket instanceof ClientboundAnimatePacket packet) {
            if (packet.getId() != player.fabricPlayer.getId()) return false;
            if (packet.getAction() != ClientboundAnimatePacket.SWING_MAIN_HAND && packet.getAction() != ClientboundAnimatePacket.SWING_OFF_HAND) return false;

            click3 = click2;
            click2 = click1;
            click1 = System.currentTimeMillis();
        }
        return false;
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.autoClickerAAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.autoClickerACheck || !AntiCheatConfig.experimentalCheck;
    }
}
