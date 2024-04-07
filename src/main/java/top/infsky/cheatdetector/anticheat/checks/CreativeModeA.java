package top.infsky.cheatdetector.anticheat.checks;

import net.minecraft.world.level.GameType;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;

public class CreativeModeA extends Check {
    public CreativeModeA(@NotNull TRPlayer player) {
        super("CreativeModeA", player);
    }

    @Override
    public void _onGameTypeChange() {
        if (player.currentGameType == GameType.CREATIVE)
            flag(true);
    }
}
