package top.infsky.cheatdetector.mixins;


import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Connection.class)
public interface ConnectionInvoker {
    @Invoker("sendPacket")
    void sendPacket(Packet<?> basePacket, PacketSendListener listener);
}
