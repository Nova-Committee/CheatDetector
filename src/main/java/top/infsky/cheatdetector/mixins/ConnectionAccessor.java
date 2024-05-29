package top.infsky.cheatdetector.mixins;


import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Connection.class)
public interface ConnectionAccessor {
    @Invoker("sendPacket")
    void sendPacket(Packet<?> basePacket, PacketSendListener listener, boolean bl);
    @Invoker("doSendPacket")
    void doSendPacket(Packet<?> packet, @Nullable PacketSendListener packetSendListener, boolean bl);
    @Invoker("channelRead0")
    void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet);
}
