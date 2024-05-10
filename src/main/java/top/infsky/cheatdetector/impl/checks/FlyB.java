package top.infsky.cheatdetector.impl.checks;

import net.minecraft.world.entity.player.Abilities;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.config.AdvancedConfig;


public class FlyB extends Check {
    public boolean hasFlag = false;

    public FlyB(@NotNull TRPlayer player) {
        super("FlyB", player);
    }

    @Override
    public void _onTick() {
        Abilities abilities = player.fabricPlayer.getAbilities();
        if (abilities.mayfly || abilities.flying) {
            if (!hasFlag) {
                flag(String.format("mayfly: %s  flying: %s", abilities.mayfly, abilities.flying));
                hasFlag = true;
            }
        } else {
            hasFlag = false;
        }
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.flyBAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.flyBCheck;
    }
}
