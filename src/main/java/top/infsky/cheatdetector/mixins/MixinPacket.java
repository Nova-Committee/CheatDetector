package top.infsky.cheatdetector.mixins;


import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.anticheat.fixs.vulcan.BadPacket1;
import top.infsky.cheatdetector.anticheat.fixs.vulcan.BadPacket2;
import top.infsky.cheatdetector.config.PacketFixer;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

@Mixin(MultiPlayerGameMode.class)
public abstract class MixinPacket {
    @Shadow public abstract void stopDestroyBlock();

    @Inject(method = "startDestroyBlock", at = @At(value = "HEAD"), cancellable = true)
    public void startDestroyBlock(BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (TRPlayer.SELF == null) return;
        TRPlayer.SELF.manager.checks.get(BadPacket1.class)._onAction(cir, false);
    }

    @Inject(method = "continueDestroyBlock", at = @At(value = "HEAD"), cancellable = true)
    public void continueDestroyBlock(BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (TRPlayer.SELF == null) return;
        if (CONFIG().getPacketFix().getPacketFixMode() != PacketFixer.STRICT) return;

        if (TRPlayer.SELF.manager.checks.get(BadPacket1.class)._onAction(cir, true)) {
            this.stopDestroyBlock();
        }
    }

    @Inject(method = "stopDestroyBlock", at = @At(value = "HEAD"), cancellable = true)
    public void stopDestroyBlock(CallbackInfo ci) {
        if (TRPlayer.SELF == null) return;
        if (CONFIG().getPacketFix().getPacketFixMode() != PacketFixer.STRICT) return;

        TRPlayer.SELF.manager.checks.get(BadPacket2.class)._onAction(ci);
    }
}
