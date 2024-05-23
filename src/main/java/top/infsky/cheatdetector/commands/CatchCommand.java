package top.infsky.cheatdetector.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.utils.LogUtils;
import top.infsky.cheatdetector.utils.TRPlayer;

public class CatchCommand {
    public static int execute(@NotNull CommandContext<FabricClientCommandSource> context) {
        String name;

        try {
            name = context.getArgument("name", String.class);
        } catch (IllegalArgumentException e) {
            if (TRPlayer.CLIENT.crosshairPickEntity instanceof Player target) {
                name = target.getName().getString();
            } else {
                name = Advanced3Config.autoCatchName;
            }
        }

        if (CheatDetector.CONFIG_HANDLER.configManager.setValue("autoCatchName", name)) {
            LogUtils.custom(ChatFormatting.GREEN + "已设置: " + ChatFormatting.WHITE + name);
            return 1;
        } else {
            return -1;
        }
    }
}
