package top.infsky.cheatdetector.impl.modules.pas;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.impl.utils.world.LevelUtils;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.utils.TRSelf;

public class Fly extends Module {
    @Getter
    @Nullable
    private static Module instance = null;
    @Nullable
    private BlockPos fakeBlockPos = null;
    private State state = State.NONE;
    private Vec3 doneFlyingPos = Vec3.ZERO;
    private boolean hasFly = false;
    @Getter
    private boolean noJump = false;

    public Fly(@NotNull TRSelf player) {
        super("Fly", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) {
            if (state == State.FLYING && !player.fabricPlayer.getBlockStateOn().is(Blocks.BARRIER)) {
                CheatDetector.CONFIG_HANDLER.configManager.setValue("flyEnabled", true);
                state = State.AFTER_FLYING;
                return;
            }
            if (state == State.AFTER_FLYING) {
                TRPlayer.CLIENT.options.keyShift.setDown(false);
            }
            state = State.NONE;
            hasFly = false;
            noJump = false;
            doneFlyingPos = Vec3.ZERO;
        } else if (state == State.NONE) {
            state = State.FLYING;
        }

        ClientLevel level = LevelUtils.getClientLevel();
        if (level == null) return;

        switch (state) {
            case NONE -> {
                if (fakeBlockPos != null)
                    if (level.getBlockState(fakeBlockPos).is(Blocks.BARRIER))
                        level.setBlock(fakeBlockPos, Blocks.AIR.defaultBlockState(), 3);
                fakeBlockPos = null;
            }
            case FLYING -> {
                noJump = true;
                BlockPos current = player.fabricPlayer.blockPosition().below();

                TRPlayer.CLIENT.options.keyShift.setDown(!fly(level, current) && hasFly);
                BlockState blockState = player.fabricPlayer.getBlockStateOn();
                if (blockState.is(Blocks.BARRIER))
                    hasFly = true;

                doneFlyingPos = player.currentPos;
            }
            case AFTER_FLYING -> {
                TRPlayer.CLIENT.options.keyShift.setDown(false);
                if (!hasFly) {
                    state = State.NONE;
                    CheatDetector.CONFIG_HANDLER.configManager.setValue("flyEnabled", false);
                    return;
                }

                if (player.currentPos.y() == doneFlyingPos.y() && player.currentOnGround) {
                    noJump = false;
                    player.fabricPlayer.jumpFromGround();
                } else if (player.currentPos.y() >= doneFlyingPos.y() + 1) {
                    noJump = true;
                    BlockPos current = player.fabricPlayer.blockPosition().atY((int) doneFlyingPos.y());
                    fly(level, current);
                }
            }
        }
    }

    public boolean fly(ClientLevel level, BlockPos current) {
        if (current == fakeBlockPos) return true;

        if (fakeBlockPos != null) {
            if (level.getBlockState(fakeBlockPos).is(Blocks.BARRIER))
                level.setBlock(fakeBlockPos, Blocks.AIR.defaultBlockState(), 3);
            fakeBlockPos = null;
        }
        if (level.getBlockState(current).isAir()) {
            level.setBlock(current, Blocks.BARRIER.defaultBlockState(), 3);
            fakeBlockPos = current;
            return true;
        }
        return false;
    }

    @Override
    public boolean _onPacketReceive(@NotNull Packet<ClientGamePacketListener> basePacket, Connection connection, ChannelHandlerContext channelHandlerContext, @NotNull CallbackInfo ci) {
        if (ci.isCancelled()) return false;

        if (state == State.NONE) return false;
        if (basePacket instanceof ClientboundPlayerPositionPacket packet) {
            if (state == State.AFTER_FLYING
                    && new Vec3(packet.getX(), packet.getY(), packet.getZ()).distanceTo(doneFlyingPos) < Advanced3Config.flyAcceptTeleportMaxDistance) {
                TRPlayer.CLIENT.options.keyShift.setDown(false);
                state = State.NONE;
                CheatDetector.CONFIG_HANDLER.configManager.setValue("flyEnabled", false);
                return false;
            }
            ci.cancel();
            return true;
        }
        return false;
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.flyEnabled || !ModuleConfig.aaaPASModeEnabled;
    }

    public enum State {
        NONE,
        FLYING,
        AFTER_FLYING
    }
}
