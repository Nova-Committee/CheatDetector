package top.infsky.cheatdetector.anticheat.modules;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.cheatdetector.anticheat.Module;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.anticheat.TRSelf;
import top.infsky.cheatdetector.config.ModuleConfig;

public class AirWalk extends Module {
    @Nullable
    private BlockPos fakeBlockPos = null;
    public AirWalk(@NotNull TRSelf player) {
        super("AirWalk", player);
    }

    @Override
    public void _onTick() {
        final ClientLevel level = TRPlayer.CLIENT.level;
        if (isDisabled()) {
            if (fakeBlockPos != null && level != null) {
                if (level.getBlockState(fakeBlockPos).is(Blocks.BARRIER)) level.setBlock(fakeBlockPos, Blocks.AIR.defaultBlockState(), 3);
            }
            fakeBlockPos = null;
            return;
        }
        if (level == null) return;

        final BlockPos current = player.fabricPlayer.blockPosition().below();
        if (current == fakeBlockPos) return;

        if (fakeBlockPos != null) {
            if (level.getBlockState(fakeBlockPos).is(Blocks.BARRIER)) level.setBlock(fakeBlockPos, Blocks.AIR.defaultBlockState(), 3);
            fakeBlockPos = null;
        }
        if (level.getBlockState(current).isAir()) {
            level.setBlock(current, Blocks.BARRIER.defaultBlockState(), 3);
            fakeBlockPos = current;
        }

    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.airWalkEnabled;
    }
}
