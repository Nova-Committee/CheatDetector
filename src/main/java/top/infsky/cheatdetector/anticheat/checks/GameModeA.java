package top.infsky.cheatdetector.anticheat.checks;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;
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
    protected int getAlertBuffer() {
        return AdvancedConfig.gameModeAAlertBuffer;
    }

    @Override
    protected boolean isDisabled() {
        return !AdvancedConfig.gameModeACheck;
    }
}

