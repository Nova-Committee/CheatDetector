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
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.impl.fixes.vulcan.BadPacket1;
import top.infsky.cheatdetector.config.utils.Fixes;
import top.infsky.cheatdetector.config.FixesConfig;

@Mixin(MultiPlayerGameMode.class)
public abstract class MixinMultiPlayerGameMode {
    @Shadow public abstract void stopDestroyBlock();

    @Inject(method = "startDestroyBlock", at = @At(value = "HEAD"), cancellable = true)
    public void startDestroyBlock(BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (TRSelf.getInstance() == null) return;
        TRSelf.getInstance().manager.onCustomAction((check) -> check._handleStartDestroyBlock(cir, false));
    }

    @Inject(method = "continueDestroyBlock", at = @At(value = "HEAD"), cancellable = true)
    public void continueDestroyBlock(BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (TRSelf.getInstance() == null) return;
        if (FixesConfig.getPacketFixMode() != Fixes.STRICT) return;

        if (TRSelf.getInstance().manager.getChecks().get(BadPacket1.class)._handleStartDestroyBlock(cir, true))
            this.stopDestroyBlock();
        TRSelf.getInstance().manager.onCustomAction((check) -> {
            if (check.getClass() != BadPacket1.class) check._handleStartDestroyBlock(cir, true);
        });
    }

    @Inject(method = "stopDestroyBlock", at = @At(value = "HEAD"), cancellable = true)
    public void stopDestroyBlock(CallbackInfo ci) {
        if (TRSelf.getInstance() == null) return;
        if (FixesConfig.getPacketFixMode() != Fixes.STRICT) return;

        TRSelf.getInstance().manager.onCustomAction((check) -> check._handleStopDestroyBlock(ci));
    }
}
