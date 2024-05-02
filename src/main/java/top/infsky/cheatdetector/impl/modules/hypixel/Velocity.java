package top.infsky.cheatdetector.impl.modules.hypixel;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.config.ModuleConfig;

public class Velocity extends Module {
    public Velocity(@NotNull TRSelf player) {
        super("Velocity", player);
    }

    @Override
    public boolean _onPacketReceive(Packet<?> basePacket, Connection connection, ChannelHandlerContext channelHandlerContext, CallbackInfo ci) {
        if (isDisabled()) return false;
        if (Advanced3Config.velocityOnlyHurt && player.fabricPlayer.hurtTime <= 0) return false;
        if (ci.isCancelled()) return false;

        if (basePacket instanceof ClientboundSetEntityMotionPacket packet) {
            if (packet.getId() != player.fabricPlayer.getId()) return false;

            ci.cancel();
            return true;
        }

        return false;
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.velocityEnabled || !ModuleConfig.aaaHypixelModeEnabled;
    }
}
