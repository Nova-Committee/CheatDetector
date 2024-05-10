package top.infsky.cheatdetector.impl.modules.common;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerLookAtPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.mixins.ConnectionInvoker;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.config.ModuleConfig;

public class NoRotateSet extends Module {
    @Getter
    @Nullable
    private static Module instance = null;
    public NoRotateSet(@NotNull TRSelf player) {
        super("NoRotateSet", player);
        instance = this;
    }

    @Override
    public boolean _onPacketReceive(@NotNull Packet<?> basePacket, Connection connection, ChannelHandlerContext channelHandlerContext, CallbackInfo ci) {
        if (isDisabled() || ci.isCancelled()) return false;
        if (basePacket instanceof ClientboundPlayerPositionPacket packet) {
            if (packet.getId() != player.fabricPlayer.getId()) return false;

            ci.cancel();
            ((ConnectionInvoker) connection).channelRead0(channelHandlerContext,
                    new ClientboundPlayerPositionPacket(
                    packet.getX(), packet.getY(), packet.getZ(), player.fabricPlayer.getYRot(), player.fabricPlayer.getXRot(), packet.getRelativeArguments(), packet.getId()
            ));
            return true;
        } else if (basePacket instanceof ClientboundPlayerLookAtPacket) {
            ci.cancel();
        }
        return false;
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.noRotateSetEnabled;
    }
}
