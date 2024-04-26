package top.infsky.cheatdetector.anticheat.utils;

import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record OutgoingPacket(@NotNull Packet<?> packet, @NotNull Connection connection, @Nullable PacketSendListener listener, long sentTime) {
}
