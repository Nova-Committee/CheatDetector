package top.infsky.cheatdetector.mixins;


import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.anticheat.fixs.themis.FastPlace;

@Mixin(Connection.class)
public class MixinConnection {
    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;)V", at = @At(value = "HEAD"), cancellable = true)
    public void send(Packet<?> basePacket, PacketSendListener packetSendListener, CallbackInfo ci) {
        if (basePacket instanceof ServerboundUseItemOnPacket packet) {
            if (TRPlayer.SELF == null) return;

            if (TRPlayer.SELF.manager.checks.get(FastPlace.class)._onAction(packet.getHand(), packet.getHitResult(), ci)) {
                if (packetSendListener != null) packetSendListener.onFailure();
            } else {
                if (packetSendListener != null) packetSendListener.onSuccess();
            }
        }
    }
}
