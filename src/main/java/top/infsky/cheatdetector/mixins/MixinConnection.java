package top.infsky.cheatdetector.mixins;


import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.impl.modules.common.Rotation;
import top.infsky.cheatdetector.utils.CheckManager;
import top.infsky.cheatdetector.utils.LogUtils;
import top.infsky.cheatdetector.utils.TRSelf;

@Mixin(value = Connection.class, priority = 1001)
public abstract class MixinConnection {
    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;)V", at = @At(value = "HEAD"), cancellable = true)
    public void send(Packet<?> basePacket, PacketSendListener listener, CallbackInfo ci) {
        if (TRSelf.getInstance() == null || !CheatDetector.inWorld) return;
        final CheckManager manager = TRSelf.getInstance().manager;

        try {
            manager.onCustomAction(check -> check._onPacketSend((Packet<ServerGamePacketListener>) basePacket, (Connection) (Object) this, listener, ci));
        } catch (ClassCastException e) {
            LogUtils.custom("Client send a Invalid Packet!");
        }
        if (!ci.isCancelled() && basePacket instanceof ServerboundMovePlayerPacket packet && packet.hasRotation()) {
            TRSelf.getInstance().rotation = new Vec2(packet.getYRot(0), packet.getXRot(0));
        }
    }

    @Inject(method = "doSendPacket", at = @At(value = "HEAD"), cancellable = true)
    public void doSendPacket(Packet<?> packet, @Nullable PacketSendListener packetSendListener, ConnectionProtocol connectionProtocol, ConnectionProtocol connectionProtocol2, CallbackInfo ci) {
        Rotation rotation = (Rotation) Rotation.getInstance();

        if (rotation != null) {
            rotation.onFinallyPacketSend((ConnectionAccessor) this, packet, packetSendListener, connectionProtocol, connectionProtocol2, ci);
        }
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/Object;)V", at = @At(value = "HEAD"), cancellable = true)
    public void channelRead0(ChannelHandlerContext channelHandlerContext, Object basePacket, CallbackInfo ci) {
        if (TRSelf.getInstance() == null || !CheatDetector.inWorld) return;
        final CheckManager manager = TRSelf.getInstance().manager;

        Packet<?> packet = (Packet<?>) basePacket;
        if (packet instanceof ClientboundPlayerPositionPacket)
            manager.onTeleport();
        try {
            manager.onCustomAction(check -> check._onPacketReceive((Packet<ClientGamePacketListener>) packet, (Connection) (Object) this, channelHandlerContext, ci));
        } catch (ClassCastException e) {
            LogUtils.custom("Server send a Invalid Packet!");
        }
    }
}
