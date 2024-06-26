package top.infsky.cheatdetector.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.utils.LogUtils;

public class NoteBotCommand {
    public static int execute(@NotNull CommandContext<FabricClientCommandSource> context) {
        String path = context.getArgument("path", String.class);
        if (CheatDetector.CONFIG_HANDLER.configManager.setValue("noteBotFilePath", path)) {
            LogUtils.custom(ChatFormatting.GREEN + "已设置: " + ChatFormatting.WHITE + path);
            return 1;
        } else {
            return -1;
        }
    }
}
