package top.infsky.cheatdetector.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;

public class BlockDetectorCommand {
    public static int execute(@NotNull CommandContext<FabricClientCommandSource> context) {
        int x, y, z;
        try {
            x = context.getArgument("x", Integer.class);
            y = context.getArgument("y", Integer.class);
            z = context.getArgument("z", Integer.class);
        } catch (IllegalArgumentException ignored) {
            if (context.getSource().getPlayer().pick(1000, 0, false) instanceof BlockHitResult hitResult) {
                if (hitResult.getType() != HitResult.Type.BLOCK) return -1;
                BlockPos blockPos = hitResult.getBlockPos();
                x = blockPos.getX();
                y = blockPos.getY();
                z = blockPos.getZ();
            } else {
                return -1;
            }
        }

        CheatDetector.CONFIG_HANDLER.configManager.setValue("blockDetectorX", x);
        CheatDetector.CONFIG_HANDLER.configManager.setValue("blockDetectorY", y);
        CheatDetector.CONFIG_HANDLER.configManager.setValue("blockDetectorZ", z);
        context.getSource().sendFeedback(Component.literal("成功设置方块检测器到坐标 " + x + " " + y + " " + z).withStyle(ChatFormatting.GREEN));
        return 1;
    }
}
