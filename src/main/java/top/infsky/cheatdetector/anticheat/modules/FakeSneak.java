package top.infsky.cheatdetector.anticheat.modules;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.Module;
import top.infsky.cheatdetector.anticheat.TRSelf;
import top.infsky.cheatdetector.config.ModuleConfig;

public class FakeSneak extends Module {
    private boolean lastEnabled = false;
    private float lastWalkSpeed = 1;
    public FakeSneak( @NotNull TRSelf player) {
        super("FakeSneak", player);
    }

    @Override
    public void _onTick() {
        if (!ModuleConfig.fakeSneakEnabled) {
            lastWalkSpeed = player.fabricPlayer.getAbilities().getWalkingSpeed();
            if (lastEnabled)
                player.fabricPlayer.getAbilities().setWalkingSpeed(lastWalkSpeed);
        } else if (!lastEnabled) {
            player.fabricPlayer.getAbilities().setWalkingSpeed(lastWalkSpeed * (10F / 3F));
        }
        lastEnabled = ModuleConfig.fakeSneakEnabled;
    }
}
