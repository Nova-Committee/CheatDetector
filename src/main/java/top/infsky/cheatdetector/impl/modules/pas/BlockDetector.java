package top.infsky.cheatdetector.impl.modules.pas;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.impl.utils.world.BlockUpdate;
import top.infsky.cheatdetector.utils.TRSelf;

public class BlockDetector extends Module {
    @Getter
    @Nullable
    private static Module instance = null;
    private BlockPos currentBlockPos;
    private BlockUpdate lastBlockUpdate;
    private long lastSendTime = -1;

    public BlockDetector(@NotNull TRSelf player) {
        super("BlockDetector", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) return;

        currentBlockPos = new BlockPos(Advanced3Config.blockDetectorX, Advanced3Config.blockDetectorY, Advanced3Config.blockDetectorZ);
        if (player.upTime - lastSendTime > Advanced3Config.blockDetectorPostDelay) {
            player.fabricPlayer.connection.send(
                    new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, currentBlockPos, player.fabricPlayer.getDirection(), 0)
            );
            player.fabricPlayer.connection.send(
                    new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK, currentBlockPos, player.fabricPlayer.getDirection(), 0)
            );
            lastSendTime = player.upTime;
        }
    }

    @Override
    public boolean _onPacketReceive(@NotNull Packet<?> basePacket, Connection connection, ChannelHandlerContext channelHandlerContext, CallbackInfo ci) {
        if (isDisabled()) return false;
        if (basePacket instanceof ClientboundBlockUpdatePacket packet) {
            if (!packet.getPos().equals(currentBlockPos)) return false;

            BlockUpdate current = new BlockUpdate(packet.getPos(), packet.getBlockState());
            if (!current.equals(lastBlockUpdate)) {
                customMsg(Component.translatable("cheatdetector.chat.alert.blockDetected")
                        .getString()
                        .formatted(current.blockPos().getX(), current.blockPos().getY(), current.blockPos().getZ(),
                                current.blockState().getBlock().getName().getString()));
            }
            lastBlockUpdate = current;
        }
        return false;
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.blockDetectorEnabled || !ModuleConfig.aaaPASModeEnabled;
    }
}
