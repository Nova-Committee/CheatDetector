package top.infsky.cheatdetector.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.impl.utils.ListUtils;

import java.util.NoSuchElementException;

public class DebugCommand {
    public static int execute(@NotNull CommandContext<FabricClientCommandSource> context) {
        String name = context.getArgument("name", String.class);

        try {
            context.getSource().sendFeedback(Component.literal(ListUtils.getSpilt(
                    CheatDetector.manager.getDataMap().values()
                    .stream()
                    .filter(trPlayer -> trPlayer.fabricPlayer.getName().getString().endsWith(name))
                    .findAny()
                    .orElseThrow()
                    .motionHistory)));
            return 1;
        } catch (NoSuchElementException e) {
            return 0;
        }
    }
}
