package top.infsky.cheatdetector.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.LogUtils;
import top.infsky.cheatdetector.utils.TRSelf;

public class ToggleCommand {
    public static int execute(@NotNull CommandContext<FabricClientCommandSource> context) {
        try {
            String moduleName = getModule(context.getArgument("moduleName", String.class)).checkName;
            String settingName = moduleName.substring(0, 1).toLowerCase() + moduleName.substring(1) + "Enabled";
            boolean baseValue = CheatDetector.CONFIG_HANDLER.configManager.getConfig(boolean.class, settingName)
                    .orElseThrow(() -> new IllegalArgumentException("不正确的设置名: " + settingName));

            CheatDetector.CONFIG_HANDLER.configManager.setValue(settingName,
                    !baseValue);
            LogUtils.custom((baseValue ? ChatFormatting.RED + "已禁用" : ChatFormatting.GREEN + "已启用") + ChatFormatting.WHITE + " " + moduleName);
            return 1;
        } catch (IllegalArgumentException e) {
            LogUtils.custom(ChatFormatting.DARK_RED + e.getMessage());
            return -1;
        }
    }

    private static @NotNull Module getModule(String input) throws IllegalArgumentException {
        for (Check check : TRSelf.getInstance().manager.getChecks().values()) {
            if (check instanceof Module module) {
                String moduleName = module.checkName;
                if (!moduleName.equalsIgnoreCase(input)) continue;

                return module;
            }
        }
        throw new IllegalArgumentException("不正确的模块茗: " + input);
    }
}
