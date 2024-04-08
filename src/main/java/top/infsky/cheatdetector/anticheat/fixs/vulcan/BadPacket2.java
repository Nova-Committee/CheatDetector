package top.infsky.cheatdetector.anticheat.fixs.vulcan;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

/**
 * Fix BadPacket2 Check.
 */
public class BadPacket2 extends Check {
    public boolean hasSend = false;

    public BadPacket2(@NotNull TRPlayer player) {
        super("BadPacket (Type 2)", player);
    }

    /**
     * @return 是否取消
     */
    @Override
    public boolean _onAction(CallbackInfo ci) {
        if (!CONFIG().getPacketFix().isPacketFixEnabled()) return false;

        if (!hasSend) {
            hasSend = true;
            return false;
        }

        if (CONFIG().getAlert().isAllowAlertPacketFix()) flag("Too many stop dig packet.");
        hasSend = false;
        ci.cancel();
        return true;
    }

    @Override
    public void _onTick() {
        hasSend = false;
    }
}
