package top.infsky.cheatdetector.impl.checks.combat;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.config.AntiCheatConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.impl.utils.world.BlockUtils;
import top.infsky.cheatdetector.impl.utils.world.LevelUtils;
import top.infsky.cheatdetector.impl.utils.world.RayCastUtils;
import top.infsky.cheatdetector.utils.TRPlayer;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class HitBoxA extends Check {
    private boolean lastSwing = false;
    public HitBoxA(@NotNull TRPlayer player) {
        super("*HitBoxA*", player);
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
                ClientLevel level = LevelUtils.getClientLevel();
                LivingEntity target = LevelUtils.getEntities(level).stream()
                        .filter(entity -> !entity.is(player.fabricPlayer))
                        .filter(entity -> entity.hurtTime >= 10 - AdvancedConfig.hitBoxACheckDelay)
                        .filter(entity -> entity.distanceTo(player.fabricPlayer) <= 6)
                        .min((e1, e2) -> (int) ((e1.distanceTo(player.fabricPlayer) - e2.distanceTo(player.fabricPlayer)) * 100))
                        .orElseThrow();

                // 此时至少有任何一个可能的目标被命中，那么检查是否击中墙壁
                BlockHitResult hitResult = RayCastUtils.blockRayCast(player.fabricPlayer, LevelUtils.getClientLevel(), player.fabricPlayer.distanceTo(target));

                if (hitResult.getType() != HitResult.Type.MISS && BlockUtils.isFullBlock(LevelUtils.getClientLevel().getBlockState(hitResult.getBlockPos()))) {
                    flag("Impossible hit.");
                }
            } catch (NoSuchElementException ignored) {}
        }, AdvancedConfig.hitBoxACheckDelay * 50L, TimeUnit.MILLISECONDS);

    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.hitBoxAAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.hitBoxACheck || !AntiCheatConfig.experimentalCheck;
    }
}
