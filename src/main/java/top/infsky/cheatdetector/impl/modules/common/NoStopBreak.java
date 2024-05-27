package top.infsky.cheatdetector.impl.modules.common;

import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.TRSelf;

public class NoStopBreak extends Module {
    public NoStopBreak(@NotNull TRSelf player) {
        super("NoStopBreak", player);
    }

    @Override
    public boolean _handleStopDestroyBlock(CallbackInfo ci) {
        if (isDisabled() || ci.isCancelled()) return false;
        if (Advanced3Config.noStopBreakSilent) return false;

        ci.cancel();
        return true;
    }

    @Override
    public boolean _onPacketSend(@NotNull Packet<ServerGamePacketListener> basePacket, Connection connection, PacketSendListener listener, CallbackInfo ci) {
        if (isDisabled() || ci.isCancelled()) return false;
        if (!Advanced3Config.noStopBreakSilent) return false;

        if (basePacket instanceof ServerboundPlayerActionPacket packet && packet.getAction() == ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK) {
            ci.cancel();
            return true;
        }
        return false;
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.noStopBreakEnabled;
    }
}
