package top.infsky.cheatdetector.impl.modules.pas;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.impl.utils.world.LevelUtils;
import top.infsky.cheatdetector.impl.utils.world.PlayerMove;
import top.infsky.cheatdetector.utils.TRSelf;

public class Speed extends Module {
    @Getter
    @Nullable
    private static Module instance = null;
    @Getter
    private boolean noJump = false;
    public Speed(@NotNull TRSelf player) {
        super("Speed", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) {
            noJump = false;
            return;
        }
        noJump = true;

        switch (player.offGroundTicks) {
            case 0 -> {
                if (PlayerMove.isMove()) {
                    player.fabricPlayer.jumpFromGround();
                }

                if (player.fabricPlayer.hasEffect(MobEffects.MOVEMENT_SPEED)) {
                    PlayerMove.strafe(0.6);
                } else {
                    PlayerMove.strafe(0.485);
                }
            }
            case 1, 2 -> PlayerMove.strafe();
            case 5 -> {
                Vec3 currentMotion = player.fabricPlayer.getDeltaMovement();
                player.fabricPlayer.setDeltaMovement(currentMotion.x(), PlayerMove.predictedMotion(currentMotion.y(), Advanced3Config.speedBoost), currentMotion.z());
            }
            case 9 -> {
                if (!LevelUtils.getClientLevel().getBlockState(
                        BlockPos.containing(player.fabricPlayer.position().add(0, player.fabricPlayer.getDeltaMovement().y(), 0))
                ).isAir()) {
                    PlayerMove.strafe();
                }
            }
        }
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.speedEnabled || !ModuleConfig.aaaPASModeEnabled;
    }
}
