package top.infsky.cheatdetector.impl.utils.world;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.ItemStack;

public class EntityUtils {
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
    private static <T extends LivingEntity> AttributeSupplier getDefaultForEntity(T entity) {
        return DefaultAttributes.getSupplier((EntityType<? extends LivingEntity>) entity.getType());
    }
}
