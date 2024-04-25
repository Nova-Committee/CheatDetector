package top.infsky.cheatdetector.mixins;


import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.anticheat.TRSelf;

@Mixin(ClientPacketListener.class)
public abstract class MixinPacket {
    @Inject(method = "handleMovePlayer", at = @At(value = "HEAD"), cancellable = true)
    public void handleMovePlayer(ClientboundPlayerPositionPacket packet, CallbackInfo ci) {
        if (TRSelf.getInstance() == null) return;

        TRSelf.getInstance().manager.onCustomAction((check) -> check._handleMovePlayer(packet, ci));
        TRSelf.getInstance().manager.onTeleport();
    }

    @Inject(method = "handlePlayerInfoUpdate", at = @At(value = "HEAD"), cancellable = true)
    public void handlePlayerInfoUpdate(ClientboundPlayerInfoUpdatePacket packet, CallbackInfo ci) {
        if (TRSelf.getInstance() == null) return;

        TRSelf.getInstance().manager.onCustomAction((check) -> check._handlePlayerInfoUpdate(packet, ci));
    }
}
