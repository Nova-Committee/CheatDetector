package top.infsky.cheatdetector.impl.modules.common;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.impl.utils.book.AishaBook1;
import top.infsky.cheatdetector.impl.utils.book.AishaBook2;
import top.infsky.cheatdetector.impl.utils.book.Book;
import top.infsky.cheatdetector.utils.TRSelf;

import java.util.List;
import java.util.Optional;

public class Writer extends Module {
    public Writer(@NotNull TRSelf player) {
        super("Writer", player);
    }

    @Override
    public void _onTick() {
        if (isDisabled()) return;

        if (!player.fabricPlayer.getMainHandItem().is(Items.WRITABLE_BOOK)) {
            customMsg(Component.translatable("cheatdetector.chat.alert.writerNeed").getString().formatted(Items.WRITABLE_BOOK.getDefaultInstance().getDisplayName().getString()));
            CheatDetector.CONFIG_HANDLER.configManager.setValue("writerEnabled", false);
            return;
        }

        player.fabricPlayer.connection.send(getPacket(player.fabricPlayer.getInventory().selected));
        CheatDetector.CONFIG_HANDLER.configManager.setValue("writerEnabled", false);
    }

    private @NotNull ServerboundEditBookPacket getPacket(int slot) {
        Book book;
        switch (Advanced3Config.writerPart) {
            case 1 -> book = new AishaBook1();
            case 2 -> book = new AishaBook2();
            default -> book = new Book() {
                @Override
                public @NotNull String getTitle() {
                    return "";
                }

                @Override
                public @NotNull List<String> getString() {
                    return List.of();
                }
            };
        }

        return new ServerboundEditBookPacket(slot, book.getBook(), Advanced3Config.writerToBook ? Optional.of(book.getTitle()) : Optional.empty());
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.writerEnabled;
    }
}
