package top.infsky.cheatdetector.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;

public class WriterCommand {
    public static int execute(@NotNull CommandContext<FabricClientCommandSource> context) {
        int part = context.getArgument("part", Integer.class);
        if (CheatDetector.CONFIG_HANDLER.configManager.setValue("writerPart", part)) {
            CheatDetector.CONFIG_HANDLER.configManager.setValue("writerEnabled", true);
            return 1;
        } else {
            return -1;
        }
    }
}
