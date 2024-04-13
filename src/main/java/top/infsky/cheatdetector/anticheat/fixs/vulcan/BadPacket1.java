package top.infsky.cheatdetector.anticheat.fixs.vulcan;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.infsky.cheatdetector.anticheat.Fix;
import top.infsky.cheatdetector.anticheat.TRPlayer;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

/**
 * Fix BadPacket1 Check.
 */
public class BadPacket1 extends Fix {
    public boolean hasSend = false;

    public BadPacket1(@NotNull TRPlayer player) {
        super("BadPacket (Type 1)", player);
    }

    /**
     * @return 是否取消
     */
    @Override
    public <T> boolean _handleStartDestroyBlock(CallbackInfoReturnable<T> cir, T fallbackReturn) {
        if (!CONFIG().getFixes().isPacketFixEnabled()) return false;

        if (!hasSend) {
            hasSend = true;
            return false;
        }

        flag("Too many start dig Packet.");
        hasSend = false;
        if (!isDisabled()) {
            cir.setReturnValue(fallbackReturn);
        }
        return true;
    }

    @Override
    public void _onTick() {
        hasSend = false;
    }

    @Override
    public boolean isDisabled() {
        return !CONFIG().getAdvanced2().isBadPacket1Enabled();
    }

    @Override
    public long getAlertBuffer() {
        return CONFIG().getAdvanced2().getBadPacket1AlertBuffer();
    }
}
