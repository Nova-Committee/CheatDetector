package top.infsky.cheatdetector.anticheat;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.checks.*;

import java.util.ArrayList;
import java.util.List;

public class CheckManager {
    public final TRPlayer player;
    public List<Check> checks = new ArrayList<>();
    public short disableTick;
    public CheckManager(List<Check> checks, TRPlayer player) {
        this.player = player;
        this.checks.addAll(checks);
        this.disableTick = 10;
    }

    @Contract("_ -> new")
    public static @NotNull CheckManager create(TRPlayer player) {
        final CheckManager checkManager = new CheckManager(List.of(
                new FlightA(player),
                new BlinkA(player),
                new SpeedA(player),
                new SpeedB(player),
                new HighJumpA(player),
                new NoSlowA(player),
                new CreativeModeA(player)
        ), player);
        checkManager.onTeleport();
        return checkManager;
    }

    public void update() {
        if (disableTick > 0) {
            disableTick--;
            return;
        }
        if (player.currentGameType != player.lastGameType) {
            for (Check check : checks) check._onGameTypeChange();
        }

        if (player.fabricPlayer.isSpectator() || player.fabricPlayer.isCreative()) return;
        if (player.lastOnGround && !player.fabricPlayer.onGround()) onJump();

        for (Check check : checks) {
            check._onTick();
        }
    }

    public void onTeleport() {
        for (Check check : checks) {
            check._onTeleport();
        }
    }

    public void onJump() {
        player.jumping = true;
        for (Check check : checks) {
            check._onJump();
        }
    }
}
