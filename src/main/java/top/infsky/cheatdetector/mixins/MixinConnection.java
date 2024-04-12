package top.infsky.cheatdetector.mixins;


import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.anticheat.fixs.Spin;
import top.infsky.cheatdetector.anticheat.fixs.themis.FastPlace;

import java.util.Map;

@Mixin(Connection.class)
public abstract class MixinConnection {
    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;)V", at = @At(value = "HEAD"), cancellable = true)
    public void send(Packet<?> basePacket, PacketSendListener listener, CallbackInfo ci) {
        if (TRPlayer.SELF == null) return;
        final Map<Class<? extends Check>, Check> checks = TRPlayer.SELF.manager.checks;

        if (basePacket instanceof ServerboundUseItemOnPacket packet)
            checks.get(FastPlace.class)._handleUseItemOn(packet, ci);
        if (basePacket instanceof ServerboundMovePlayerPacket packet)
            checks.get(Spin.class)._handleMovePlayer(packet, (Connection)(Object) this, listener, ci);
    }
}
