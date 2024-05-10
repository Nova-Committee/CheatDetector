package top.infsky.cheatdetector.impl.modules.common;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;
import oshi.util.tuples.Triplet;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.impl.utils.AimSimulator;
import top.infsky.cheatdetector.impl.utils.world.LevelUtils;
import top.infsky.cheatdetector.impl.utils.world.PlayerRotation;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.utils.TRSelf;

public class AimAssist extends Module {
    public AimAssist(@NotNull TRSelf player) {
        super("AimAssist", player);
    }

    private boolean checkTarget(@NotNull Entity entity) {
        if (entity.is(player.fabricPlayer)) return false;
        if (entity instanceof Player) return Advanced3Config.aimAssistIncludePlayers;
        if (entity instanceof ArmorStand) return Advanced3Config.aimAssistIncludeArmorStands;
        if (entity instanceof LivingEntity) return Advanced3Config.aimAssistIncludeEntities;
        return Advanced3Config.aimAssistIncludeAnyObjects;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) return;

        double distance = Advanced3Config.aimAssistRange;
        Entity target = null;
        for (Entity entity : LevelUtils.getClientLevel().entitiesForRendering()) {
            if (checkTarget(entity) && distance > entity.distanceTo(player.fabricPlayer)) {
                target = entity;
                distance = entity.distanceTo(player.fabricPlayer);
            }
        }

        if (target != null)
            rotate(target);
    }

    private void rotate(Entity target) {
        Pair<Double, Double> rot;

        if (Advanced3Config.aimAssistInteract) {
            rot = new Pair<>(
                    PlayerRotation.getYaw(target.getEyePosition()),
                    PlayerRotation.getPitch(target.getEyePosition())
            );
        } else {
            if (Advanced3Config.aimAssistStopOnTarget && TRPlayer.CLIENT.crosshairPickEntity == target) return;

            rot = AimSimulator.getLegitAim(target, player,
                    new Pair<>(Advanced3Config.aimAssistYawSpeed, Advanced3Config.aimAssistPitchSpeed),
                    new Triplet<>(Advanced3Config.aimAssistOffsetX, Advanced3Config.aimAssistOffsetY, Advanced3Config.aimAssistOffsetZ),
                    Advanced3Config.aimAssistNoise1,
                    new Pair<>(Advanced3Config.aimAssistYawRandom, Advanced3Config.aimAssistPitchRandom),
                    Advanced3Config.aimAssistNoise2,
                    new Pair<>(Advanced3Config.aimAssistXZRandom, Advanced3Config.aimAssistYRandom)
            );
        }

        PlayerRotation.rotate(rot.getA(), rot.getB());
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.aimAssistEnabled;
    }
}
