package top.infsky.cheatdetector.impl.fixes.watchdog;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.FixesConfig;
import top.infsky.cheatdetector.impl.Fix;
import top.infsky.cheatdetector.utils.TRSelf;

public class NoSlowDisabler extends Fix {
    public NoSlowDisabler(@NotNull TRSelf player) {
        super("NoSlowDisabler", player);
    }

    @Override
    public void _onTick() {
        if (isDisabled()) return;

        if (player.fabricPlayer.isUsingItem()) {
            player.fabricPlayer.connection.send(
                    new ServerboundUseItemOnPacket(
                            InteractionHand.MAIN_HAND,
                            new BlockHitResult(player.currentPos, Direction.DOWN, new BlockPos(0, 0, 0), false)
                            , 0));
        }
    }

    @Override
    public boolean isDisabled() {
        return !FixesConfig.watchdogNoSlowDisablerEnabled;
    }
}
