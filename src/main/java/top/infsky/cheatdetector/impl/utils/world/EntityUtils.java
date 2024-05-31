package top.infsky.cheatdetector.impl.utils.world;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.utils.TRPlayer;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class EntityUtils {
    public static @NotNull Optional<String> getNonBotPlayerName(UUID uuid) {
        TRPlayer trPlayer = CheatDetector.manager.getDataMap().getOrDefault(uuid, null);
        if (trPlayer != null) {
            return Optional.of(trPlayer.fabricPlayer.getGameProfile().getName());
        }

        PlayerInfo playerInfo = Objects.requireNonNull(TRPlayer.CLIENT.getConnection()).getPlayerInfo(uuid);
        if (playerInfo != null) {
            return Optional.of(playerInfo.getProfile().getName());
        }

        return Optional.empty();
    }


    /**
     * @see LivingEntity#getAttributes()
     */
    public static AttributeMap getAttributes(LivingEntity entity) {
        AttributeMap attributes = new AttributeMap(getDefaultForEntity(entity));

        // Equipment
        for (var equipmentSlot : EquipmentSlot.values()) {
            ItemStack stack = entity.getItemBySlot(equipmentSlot);
            attributes.addTransientAttributeModifiers(stack.getAttributeModifiers(equipmentSlot));
        }

        // Status effects
        for (var statusEffect : entity.getActiveEffectsMap().values()) {
            statusEffect.getEffect().addAttributeModifiers(entity, attributes, statusEffect.getAmplifier());
        }

        return attributes;
    }

    @SuppressWarnings("unchecked")
    private static <T extends LivingEntity> @NotNull AttributeSupplier getDefaultForEntity(@NotNull T entity) {
        return DefaultAttributes.getSupplier((EntityType<? extends LivingEntity>) entity.getType());
    }

    public static @NotNull Optional<AbstractClientPlayer> getClosestPlayer(String target, AbstractClientPlayer player) {
        return LevelUtils.getClientLevel().players()
                .stream()
                .filter(p -> p.getName().getString().endsWith(target))
                .min((p1, p2) -> (int) (p1.distanceTo(player) - p2.distanceTo(player)));
    }
}
