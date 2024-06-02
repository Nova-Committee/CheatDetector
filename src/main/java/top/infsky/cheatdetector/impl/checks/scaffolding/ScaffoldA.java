package top.infsky.cheatdetector.impl.checks.scaffolding;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.config.AntiCheatConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.utils.TRPlayer;

public class ScaffoldA extends Check {
    public ScaffoldA(@NotNull TRPlayer player) {
        super("*ScaffoldA*", player);
    }

    @Override
    public void _onPlaceBlock(InteractionHand hand, BlockPos blockPos) {
        if (!player.currentSwing || player.fabricPlayer.swingingArm != hand) {
            flag("no valid swing. block: %s".formatted(blockPos));
        }
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.scaffoldAAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.scaffoldACheck || !AntiCheatConfig.experimentalCheck;
    }
}
