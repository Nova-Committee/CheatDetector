package top.infsky.cheatdetector.impl.modules.common;

import net.minecraft.world.InteractionHand;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.TRSelf;

import java.util.ArrayList;
import java.util.List;

public class HandSpin extends Module {
    private long lastSwingTime = 0;
    private InteractionHand lastDiffSwingHand = InteractionHand.OFF_HAND;
    public HandSpin( @NotNull TRSelf player) {
        super("HandSpin", player);
    }

    @Override
    public void _onTick() {
        if (isDisabled()) return;
        if (Advanced3Config.handSpinPerfectSwing && player.fabricPlayer.swinging) return;
        if (player.upTime - lastSwingTime < Advanced3Config.handSpinSwingDelay) return;

        for (InteractionHand hand : getSwingHands()) {
            player.fabricPlayer.swing(hand);
        }
        lastSwingTime = player.upTime;
    }

    private @NotNull List<InteractionHand> getSwingHands() {
        List<InteractionHand> hands = new ArrayList<>(2);
        if (Advanced3Config.handSpinMainHand) hands.add(InteractionHand.MAIN_HAND);
        if (Advanced3Config.handSpinOffHand) hands.add(InteractionHand.OFF_HAND);

        if (!Advanced3Config.handSpinDiffSwing) return hands;

        if (hands.size() < 2) return hands;
        switch (lastDiffSwingHand) {
            case MAIN_HAND -> {
                lastDiffSwingHand = InteractionHand.OFF_HAND;
                return List.of(InteractionHand.OFF_HAND);
            }
            case OFF_HAND -> {
                lastDiffSwingHand = InteractionHand.MAIN_HAND;
                return List.of(InteractionHand.MAIN_HAND);
            }
        }
        return hands;
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.handSpinEnabled;
    }
}
