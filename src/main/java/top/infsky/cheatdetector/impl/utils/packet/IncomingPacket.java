package top.infsky.cheatdetector.impl.utils.packet;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.NotNull;

public record IncomingPacket(@NotNull Packet<?> packet, @NotNull Connection connection, ChannelHandlerContext context, long sentTime) {
}
