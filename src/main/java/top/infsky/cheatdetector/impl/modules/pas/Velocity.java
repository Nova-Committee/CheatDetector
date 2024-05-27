package top.infsky.cheatdetector.impl.modules.pas;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.impl.utils.world.PlayerMove;
import top.infsky.cheatdetector.utils.TRSelf;

public class Velocity extends Module {
    @Getter
    @Nullable
    private static Module instance = null;
    public Velocity( @NotNull TRSelf player) {
        super("Velocity", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) return;

        if (Advanced3Config.velocityStrafe && player.fabricPlayer.hurtTime >= Advanced3Config.velocityStrafeMinHurtTime) {
            PlayerMove.strafe();
        }
    }

    @Override
    public boolean _onPacketReceive(@NotNull Packet<ClientGamePacketListener> basePacket, Connection connection, ChannelHandlerContext channelHandlerContext, CallbackInfo ci) {
        if (isDisabled()) return false;

        if (basePacket instanceof ClientboundSetEntityMotionPacket packet) {
            if (packet.getId() != player.fabricPlayer.getId()) return false;

            ci.cancel();
            double xa = (double) packet.getXa() * Advanced3Config.velocityHorizon / 8000;
            double ya = (double) packet.getYa() * Advanced3Config.velocityVertical / 8000;
            double za = (double) packet.getZa() * Advanced3Config.velocityHorizon / 8000;

            if (Advanced3Config.velocityReserve) {
                xa *= -1;
                za *= -1;
            }

            player.fabricPlayer.lerpMotion(xa, ya, za);
            return true;
        }
        return false;
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.velocityEnabled || !ModuleConfig.aaaPASModeEnabled;
    }
}
