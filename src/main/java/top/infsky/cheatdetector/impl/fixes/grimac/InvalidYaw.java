package top.infsky.cheatdetector.impl.fixes.grimac;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.Advanced2Config;
import top.infsky.cheatdetector.impl.Fix;
import top.infsky.cheatdetector.utils.TRSelf;

public class InvalidYaw extends Fix {
    public InvalidYaw(@NotNull TRSelf player) {
        super("InvalidYaw", player);
    }

    @Override
    public void _onTick() {
        if (isDisabled()) return;
        if (Math.abs(player.fabricPlayer.getYRot()) > Advanced2Config.invalidYawMaxYaw) {
            flag("Too large yaw: %.1f°".formatted(player.fabricPlayer.getYRot()));
            player.fabricPlayer.setYRot(player.fabricPlayer.getYRot() % 180);
        }
    }

    @Override
    public int getAlertBuffer() {
        return Advanced2Config.invalidYawAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !Advanced2Config.invalidYawEnabled;
    }
}
