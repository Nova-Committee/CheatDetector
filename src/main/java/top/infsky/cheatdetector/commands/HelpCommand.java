package top.infsky.cheatdetector.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class HelpCommand {
    private static final String helpMsg = """
                §r§b§lCheatDetector 帮助信息§r
                §r/ctr (help) §f- §7显示此帮助信息§r
                §r/ctr t(toggle) <moduleName> §f- §7启用/禁用某个模块§r
                §r/ctr notebot <path> §f- §7修改音符机器人.nbs文件路径§r
                §r/ctr clientspoof <brand> §f- §7设置客户端伪装名§r
                §r/ctr writer <part> §f- §7写一本书。§r
                §r/ctr catch <name> §f- §7设置自动骑上一名玩家。§r
                §r/ctr surround <name> §f- §7设置自动用方块自动包围一名玩家。§r
                """;

    public static int execute(@NotNull CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Component.literal(helpMsg));
        return 1;
    }
}
