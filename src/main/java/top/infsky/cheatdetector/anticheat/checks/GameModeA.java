package top.infsky.cheatdetector.anticheat.checks;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

public class GameModeA extends Check {
    public GameModeA(@NotNull TRPlayer player) {
        super("GameModeA", player);
    }

    @Override
    public void _onGameTypeChange() {
        flag("Gamemode: " + player.currentGameType.getName());
    }

    @Override
    protected long getAlertBuffer() {
        return CONFIG().getAdvanced().getGameModeAAlertBuffer();
    }

    @Override
    protected boolean isDisabled() {
        return !CONFIG().getAdvanced().isGameModeACheck();
    }
}

