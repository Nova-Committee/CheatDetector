package top.infsky.cheatdetector.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;

public class NoteBotCommand {
    public static int execute(@NotNull CommandContext<FabricClientCommandSource> context) {
        if (CheatDetector.CONFIG_HANDLER.configManager.setValue("noteBotFilePath", context.getArgument("path", String.class)))
            return 1;
        else
            return -1;
    }
}
