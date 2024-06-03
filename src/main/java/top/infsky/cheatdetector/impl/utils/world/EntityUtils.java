package top.infsky.cheatdetector.impl.utils.world;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;
import oshi.util.tuples.Triplet;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.utils.TRPlayer;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
            statusEffect.getEffect().addAttributeModifiers(attributes, statusEffect.getAmplifier());
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

    public static Optional<Pair<InteractionHand, BlockPos>> isOnPlaceBlock(@NotNull TRPlayer player) {
        for (Triplet<ItemStack, ItemStack, InteractionHand> hands : Set.of(
                new Triplet<>(player.currentMainHead, player.lastMainHead, InteractionHand.MAIN_HAND),
                new Triplet<>(player.currentOffHead, player.lastOffHead, InteractionHand.OFF_HAND))
        ) {
            if (!(hands.getB().getItem() instanceof BlockItem)) continue;
            if (hands.getA().is(hands.getB().getItem()) && hands.getA().getCount() == hands.getB().getCount() - 1) {
                BlockHitResult hitResult1 = RayCastUtils.blockRayCast(player.fabricPlayer, LevelUtils.getClientLevel(), 4.5);
                if (hitResult1.getType() != HitResult.Type.BLOCK) continue;
//                BlockHitResult hitResult2 = RayCastUtils.blockRayCast(player.fabricPlayer, player.lastLevel, 4.5);
//                if (player.fabricPlayer instanceof LocalPlayer) {
//                }

//                if (hitResult2.getType() == HitResult.Type.MISS || !hitResult2.getBlockPos().equals(hitResult1.getBlockPos())) {
//                }
                return Optional.of(new Pair<>(hands.getC(), hitResult1.getBlockPos()));
            }
        }
        return Optional.empty();
    }
}
