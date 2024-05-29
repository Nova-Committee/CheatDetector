package top.infsky.cheatdetector.impl.modules.danger;

import lombok.Getter;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.protocol.common.*;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.DangerConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.impl.utils.world.LevelUtils;
import top.infsky.cheatdetector.utils.TRSelf;

import java.util.List;

@Getter
public class AirStuck extends Module {
    @Getter
    @Nullable
    private static Module instance = null;

    public static final List<Class<? extends Packet<?>>> NORMAL_CANCEL_PACKETS = List.of(
            ServerboundPongPacket.class,
            ServerboundPlayerInputPacket.class,
            ServerboundMoveVehiclePacket.class,
            ServerboundCustomPayloadPacket.class
    );

    public static final List<Class<? extends Packet<?>>> ANTI_KICK_CANCEL_PACKETS = List.of(
            ServerboundPongPacket.class,
            ServerboundPlayerInputPacket.class,
            ServerboundMoveVehiclePacket.class,
            ServerboundCustomPayloadPacket.class,
            ServerboundMovePlayerPacket.class
    );

    private boolean shouldStuck = false;
    private final Vec3 modMoveTo = new Vec3(0, 0, 0);

    public AirStuck(@NotNull TRSelf player) {
        super("AirStuck", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) {
            shouldStuck = false;
            return;
        }

        shouldStuck = check();

        if (shouldStuck) {
            player.fabricPlayer.setDeltaMovement(modMoveTo);
        }
    }

    @SuppressWarnings("RedundantIfStatement")  // java的if优化给我优化出bug来了
    private boolean check() {
        if (Advanced3Config.airStuckMinDistanceBeforeGround >= 0)
            if (Advanced3Config.airStuckMinDistanceBeforeGround < getDistanceToGround())
                return false;
        if (Advanced3Config.airStuckMinFallDistance >= 0)
            if (Advanced3Config.airStuckMinFallDistance > player.fabricPlayer.fallDistance)
                return false;
        return true;
    }

    private double getDistanceToGround() {
        BlockPos playerPos = player.fabricPlayer.blockPosition();
        ClientLevel clientLevel = LevelUtils.getClientLevel();
        for (int i = playerPos.getY(); i >= -64; i--) {
            if (clientLevel.getBlockState(new BlockPos(playerPos.getX(), i, playerPos.getZ())).isAir())
                continue;

            return player.currentPos.y() - i - 1.5;
        }
        return Double.MAX_VALUE;
    }

    @Override
    public boolean _onPacketSend(@NotNull Packet<ServerGamePacketListener> basePacket, Connection connection, PacketSendListener listener, CallbackInfo ci) {
        if (isDisabled() || !shouldStuck || !Advanced3Config.airStuckCancelPacket) return false;

        if (Advanced3Config.airStuckLegit) {
            ci.cancel();
            return true;
        } else if (Advanced3Config.airStuckAntiKick) {
            if (ANTI_KICK_CANCEL_PACKETS.stream().anyMatch(aClass -> aClass.isInstance(basePacket))) {
                ci.cancel();
                return true;
            }
        } else if (NORMAL_CANCEL_PACKETS.stream().anyMatch(aClass -> aClass.isInstance(basePacket))) {
            ci.cancel();
            return true;
        }
        return false;
    }

    @Override
    public boolean isDisabled() {
        return !DangerConfig.airStuckEnabled || !DangerConfig.aaaDangerModeEnabled;
    }
}
