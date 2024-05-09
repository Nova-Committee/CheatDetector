package top.infsky.cheatdetector.impl.utils.world;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.utils.TRPlayer;

import java.util.Objects;

public class ContainerUtils {
    public static class ItemNotFoundException extends RuntimeException {
        public ItemNotFoundException(String message) {
            super(message);
        }
    }

    public static int findItem(Container container, Class<? extends Item> itemClass, @NotNull SlotType slotType) throws ItemNotFoundException {
        for (int i = slotType.from(); i <= slotType.to(); i++) {
            if (itemClass.isInstance(container.getItem(i).getItem())) {
                return i;
            }
        }
        throw new ItemNotFoundException("Couldn't find any ItemStack which include item '%s'.".formatted(itemClass.toString()));
    }

    public static void silentSelectHotBar(int slot) {
        Objects.requireNonNull(TRPlayer.CLIENT.getConnection()).send(new ServerboundSetCarriedItemPacket(slot));
    }

    public record SlotType(int from, int to) {
        public static final SlotType HOTBAR = new SlotType(0, 8);
        public static final SlotType INVENTORY = new SlotType(0, 35);
        public static final SlotType CHEST = new SlotType(0, 26);
    }
}
