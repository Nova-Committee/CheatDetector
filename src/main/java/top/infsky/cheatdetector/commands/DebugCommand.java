package top.infsky.cheatdetector.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;

import java.util.NoSuchElementException;

public class DebugCommand {
    public static int execute(@NotNull CommandContext<FabricClientCommandSource> context) {
        String name = context.getArgument("name", String.class);

        try {
            context.getSource().sendFeedback(Component.literal("slot: %s".formatted(
                    CheatDetector.manager.getDataMap().values()
                            .stream()
                            .filter(trPlayer -> trPlayer.fabricPlayer.getName().getString().endsWith(name))
                            .findAny()
                            .orElseThrow()
                            .fabricPlayer.getInventory().selected))
            );
            return 1;
        } catch (NoSuchElementException e) {
            return 0;
        }
    }
}
