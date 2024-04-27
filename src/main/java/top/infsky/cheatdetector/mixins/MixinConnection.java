package top.infsky.cheatdetector.mixins;


import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.anticheat.CheckManager;
import top.infsky.cheatdetector.anticheat.TRSelf;

@Mixin(Connection.class)
public abstract class MixinConnection {
    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;)V", at = @At(value = "HEAD"), cancellable = true)
    public void send(Packet<?> basePacket, PacketSendListener listener, CallbackInfo ci) {
        if (TRSelf.getInstance() == null || !CheatDetector.inWorld) return;
        final CheckManager manager = TRSelf.getInstance().manager;

        manager.onCustomAction(check -> check._onPacketSend(basePacket, (Connection)(Object) this, listener, ci));
        if (!ci.isCancelled() && basePacket instanceof ServerboundUseItemOnPacket packet)
            manager.onCustomAction(check -> check._handleUseItemOn(packet, ci));
        if (!ci.isCancelled() && basePacket instanceof ServerboundMovePlayerPacket packet)
            manager.onCustomAction(check -> check._handleMovePlayer(packet, (Connection)(Object) this, listener, ci));
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/Object;)V", at = @At(value = "HEAD"), cancellable = true)
    public void channelRead0(ChannelHandlerContext channelHandlerContext, Object packet, CallbackInfo ci) {
        if (TRSelf.getInstance() == null || !CheatDetector.inWorld) return;
        final CheckManager manager = TRSelf.getInstance().manager;

        manager.onCustomAction(check -> check._onPacketReceive((Packet<?>) packet, (Connection)(Object) this, channelHandlerContext, ci));
    }
}
