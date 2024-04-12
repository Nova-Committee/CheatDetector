package top.infsky.cheatdetector.mixins;


import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.anticheat.TRPlayer;

@Mixin(ClientPacketListener.class)
public abstract class MixinPacket {
    @Inject(method = "handleMovePlayer", at = @At(value = "HEAD"))
    public void handleMovePlayer(ClientboundPlayerPositionPacket packet, CallbackInfo ci) {
        if (TRPlayer.SELF == null) return;

        TRPlayer.SELF.manager.onTeleport();
    }
}
