package top.infsky.cheatdetector.impl.checks;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.config.AntiCheatConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.impl.utils.world.LevelUtils;
import top.infsky.cheatdetector.utils.TRPlayer;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class ReachA extends Check {
    private boolean lastSwing = false;
    public ReachA(@NotNull TRPlayer player) {
        super("ReachA", player);
    }

    @Override
    public void _onTick() {
        boolean currentSwing = player.fabricPlayer.swinging;

        if (currentSwing && !lastSwing) {  // 第1个挥手tick
            onSwing();
        }
        lastSwing = currentSwing;
    }

    private void onSwing() {
        if (isDisabled()) return;

        player.timeTask.schedule(() -> {
            try {
                LivingEntity possibleTarget = LevelUtils.getEntities().stream()
                        .filter(entity -> !entity.is(player.fabricPlayer))
                        .filter(entity -> entity.hurtTime >= 10 - AdvancedConfig.reachACheckDelay)
                        .min((e1, e2) -> (int) ((e1.distanceTo(player.fabricPlayer) - e2.distanceTo(player.fabricPlayer)) * 100))
                        .orElseThrow();
                double distance = possibleTarget.distanceTo(player.fabricPlayer);
                if (distance < 6 && distance > AdvancedConfig.reachADefaultReach) {  // 满足标记条件
                    flag("distance: %.2f".formatted(distance));
                }
            } catch (NoSuchElementException ignored) {}
        }, AdvancedConfig.reachACheckDelay * 50L, TimeUnit.MILLISECONDS);

    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.reachAAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.reachACheck || !AntiCheatConfig.experimentalCheck;
    }
}
