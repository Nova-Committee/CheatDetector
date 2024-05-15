package top.infsky.cheatdetector.impl.fixes.vulcan;

import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.config.Advanced2Config;
import top.infsky.cheatdetector.impl.Fix;
import top.infsky.cheatdetector.utils.TRSelf;

public class SprintA extends Fix {
    public SprintA(@NotNull TRSelf player) {
        super("Sprint (A)", player);
    }

    @Override
    public boolean _onPacketSend(@NotNull Packet<?> basePacket, Connection connection, PacketSendListener listener, CallbackInfo ci) {
        if (isDisabled()) return false;
        if (basePacket instanceof ServerboundMovePlayerPacket packet && packet.hasRotation()) {
            final float shouldYaw = player.fabricPlayer.getYRot();
            if (packet.getYRot(shouldYaw) != shouldYaw) {
                flag();
                player.fabricPlayer.setSprinting(false);
            }
        }
        return false;
    }

    @Override
    public boolean isDisabled() {
        return !Advanced2Config.sprintAEnabled;
    }

    @Override
    public int getAlertBuffer() {
        return Advanced2Config.sprintAAlertBuffer;
    }
}
