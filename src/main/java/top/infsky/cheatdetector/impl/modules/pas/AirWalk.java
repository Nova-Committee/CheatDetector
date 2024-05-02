package top.infsky.cheatdetector.impl.modules.pas;

import lombok.Getter;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.impl.utils.world.LevelUtils;
import top.infsky.cheatdetector.impl.utils.world.PlayerMove;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;

public class AirWalk extends Module {
    @Getter
    @Nullable
    private static Module instance = null;

    @Nullable
    private BlockPos fakeBlockPos = null;
    @Nullable
    private Integer yPos = null;
    @Nullable
    private BlockPos lastFreeze = null;
    public AirWalk(@NotNull TRSelf player) {
        super("AirWalk", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        BlockPos current;
        ClientLevel level = LevelUtils.getClientLevel();
        if (isDisabled()) {
            if (fakeBlockPos != null && level != null) {
                if (level.getBlockState(fakeBlockPos).is(Blocks.BARRIER))
                    level.setBlock(fakeBlockPos, Blocks.AIR.defaultBlockState(), 3);
            }
            fakeBlockPos = null;
            yPos = null;
            return;
        }
        if (level == null) return;
        if (yPos == null) yPos = player.fabricPlayer.getBlockY() - 1;

        current = player.fabricPlayer.blockPosition().below();
        if (Advanced3Config.airWalkSameY && fakeBlockPos != null)
            current = current.atY(yPos);
        if (current == fakeBlockPos) return;

        if (fakeBlockPos != null) {
            if (level.getBlockState(fakeBlockPos).is(Blocks.BARRIER))
                level.setBlock(fakeBlockPos, Blocks.AIR.defaultBlockState(), 3);
            fakeBlockPos = null;
        }
        if (level.getBlockState(current).isAir()) {
            level.setBlock(current, Blocks.BARRIER.defaultBlockState(), 3);
            fakeBlockPos = current;
        }

        if (PlayerMove.getXzSecSpeed(player.lastPos, player.currentPos) > 1.8 && Advanced3Config.airWalkAutoJump && player.currentOnGround)
            player.fabricPlayer.jumpFromGround();
        else if (player.fabricPlayer.input.jumping)
            yPos = player.fabricPlayer.getBlockY() - 1;
        else if (player.fabricPlayer.input.shiftKeyDown)
            yPos = player.fabricPlayer.getBlockY() - 2;
        else if (Advanced3Config.airWalkAntiFlag && current.atY(yPos) != lastFreeze && player.currentOnGround) {
            player.fabricPlayer.jumpFromGround();
            yPos = player.fabricPlayer.getBlockY() - 1;
            lastFreeze = current.atY(yPos);
        }
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.airWalkEnabled || !ModuleConfig.aaaPASModeEnabled;
    }
}
