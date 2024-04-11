package top.infsky.cheatdetector.anticheat.fixs.vulcan;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.anticheat.Fix;
import top.infsky.cheatdetector.anticheat.TRPlayer;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

/**
 * Fix BadPacket2 Check.
 */
public class BadPacket2 extends Fix {
    public boolean hasSend = false;

    public BadPacket2(@NotNull TRPlayer player) {
        super("BadPacket (Type 2)", player);
    }

    /**
     * @return 是否取消
     */
    @Override
    public boolean _onAction(CallbackInfo ci) {
        if (!CONFIG().getFixes().isPacketFixEnabled()) return false;

        if (!hasSend) {
            hasSend = true;
            return false;
        }

        flag("Too many stop dig packet.");
        hasSend = false;
        if (!isDisabled())
            ci.cancel();
        return true;
    }

    @Override
    public void _onTick() {
        hasSend = false;
    }

    @Override
    public boolean isDisabled() {
        return !CONFIG().getAdvanced2().isBadPacket2Enabled();
    }

    @Override
    public long getAlertBuffer() {
        return CONFIG().getAdvanced2().getBadPacket2AlertBuffer();
    }
}
