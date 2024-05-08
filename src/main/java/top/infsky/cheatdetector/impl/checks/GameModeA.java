package top.infsky.cheatdetector.impl.checks;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.config.AdvancedConfig;

public class GameModeA extends Check {
    public GameModeA(@NotNull TRPlayer player) {
        super("GameModeA", player);
    }

    @Override
    public void _onGameTypeChange() {
        flag("Gamemode: " + player.currentGameType.getName());
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.gameModeAAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.gameModeACheck;
    }
}

