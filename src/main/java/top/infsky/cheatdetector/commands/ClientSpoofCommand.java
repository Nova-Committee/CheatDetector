package top.infsky.cheatdetector.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.utils.LogUtils;

public class ClientSpoofCommand {
    public static int execute(@NotNull CommandContext<FabricClientCommandSource> context) {
        String brand = context.getArgument("brand", String.class);
        if (CheatDetector.CONFIG_HANDLER.configManager.setValue("clientSpoofBrand", brand)) {
            LogUtils.custom(ChatFormatting.GREEN + "已设置: " + ChatFormatting.WHITE + brand);
            return 1;
        } else {
            return -1;
        }
    }
}
