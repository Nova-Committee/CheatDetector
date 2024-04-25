package top.infsky.cheatdetector.anticheat;

import lombok.Getter;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.NotNull;

public class TRSelf extends TRPlayer {
    @Getter
    private static TRSelf instance = null;
    public LocalPlayer fabricPlayer;

    public TRSelf(@NotNull LocalPlayer player) {
        super(player, true);
        this.fabricPlayer = player;
        instance = this;
    }

    public static void onDisconnect() {
        instance = null;
    }
}
