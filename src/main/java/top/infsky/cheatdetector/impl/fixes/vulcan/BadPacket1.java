package top.infsky.cheatdetector.impl.fixes.vulcan;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.infsky.cheatdetector.impl.Fix;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.config.Advanced2Config;
import top.infsky.cheatdetector.config.FixesConfig;

/**
 * Fix BadPacket1 Check.
 */
public class BadPacket1 extends Fix {
    public boolean hasSend = false;

    public BadPacket1(@NotNull TRSelf player) {
        super("BadPacket (Type 1)", player);
    }

    /**
     * @return 是否取消
     */
    @Override
    public <T> boolean _handleStartDestroyBlock(@NotNull CallbackInfoReturnable<T> cir, T fallbackReturn) {
        if (isDisabled()) return false;

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
        return !Advanced2Config.badPacket1Enabled || !FixesConfig.packetFixEnabled;
    }

    @Override
    public int getAlertBuffer() {
        return Advanced2Config.badPacket1AlertBuffer;
    }
}
