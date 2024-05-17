package top.infsky.cheatdetector.impl.modules.common;

import lombok.Getter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.TRSelf;

public class MoreKB extends Module {
    @Getter
    @Nullable
    private static Module instance = null;

    public MoreKB(@NotNull TRSelf player) {
        super("MoreKB", player);
        instance = this;
    }

    @Override
    public void _handleAttack(Entity entity) {
        if (isDisabled()) return;
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.hurtTime > 0) return;
            if (player.fabricPlayer.input.up && player.fabricPlayer.isSprinting()) {
                player.fabricPlayer.setSprinting(false);
                player.fabricPlayer.setSprinting(true);
            }
        }
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.moreKBEnabled;
    }
}
