package top.infsky.cheatdetector.mixins;


import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.infsky.cheatdetector.anticheat.fixs.vulcan.BadPacket1;
import top.infsky.cheatdetector.config.PacketFixer;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

@Mixin(MultiPlayerGameMode.class)
public abstract class MixinPacket {
    @Shadow public abstract void stopDestroyBlock();

    @Unique public BadPacket1 check = null;

    @Inject(method = "startDestroyBlock", at = @At(value = "HEAD"), cancellable = true)
    public void startDestroyBlock(BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (check == null) check = new BadPacket1();
        check._onAction(cir, false);
    }

    @Inject(method = "continueDestroyBlock", at = @At(value = "HEAD"), cancellable = true)
    public void continueDestroyBlock(BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (check == null) check = new BadPacket1();
        if (CONFIG().getPacketFixMode() != PacketFixer.STRICT) return;

        if (check._onAction(cir, true)) {
            this.stopDestroyBlock();
        }
    }
}
