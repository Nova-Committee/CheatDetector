package top.infsky.cheatdetector.impl.fixes.vulcan;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.impl.Fix;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.config.Advanced2Config;
import top.infsky.cheatdetector.config.FixesConfig;

/**
 * Fix BadPacket2 Check.
 */
public class BadPacket2 extends Fix {
    public boolean hasSend = false;

    public BadPacket2(@NotNull TRSelf player) {
        super("BadPacket (Type 2)", player);
    }

    /**
     * @return 是否取消
     */
    @Override
    public boolean _handleStopDestroyBlock(@NotNull CallbackInfo ci) {
        if (!FixesConfig.packetFixEnabled) return false;

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
        return !Advanced2Config.badPacket2Enabled;
    }

    @Override
    public int getAlertBuffer() {
        return Advanced2Config.badPacket2AlertBuffer;
    }
}
