package top.infsky.cheatdetector.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandBuildContext;
import org.jetbrains.annotations.NotNull;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

public class CommandEvent {
    public static void register(@NotNull CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext ignoredContext) {
        dispatcher.register(literal("ctr")
                .executes(HelpCommand::execute)
                .then(literal("help")
                        .executes(HelpCommand::execute))
                .then(literal("t")
                        .then(argument("moduleName", StringArgumentType.string())
                                .executes(ToggleCommand::execute)
                        )
                )
                .then(literal("toggle")
                        .then(argument("moduleName", StringArgumentType.string())
                                .executes(ToggleCommand::execute)
                        )
                )
                .then(literal("notebot")
                        .then(argument("path", StringArgumentType.string())
                                .executes(NoteBotCommand::execute)
                        )
                )
                .then(literal("blockdetector")
                        .executes(BlockDetectorCommand::execute)
                        .then(argument("x", IntegerArgumentType.integer())
                                .then(argument("y", IntegerArgumentType.integer())
                                        .then(argument("z", IntegerArgumentType.integer())
                                                .executes(BlockDetectorCommand::execute))))
                )
                .then(literal("clientspoof")
                        .then(argument("brand", StringArgumentType.string())
                                .executes(ClientSpoofCommand::execute))
                )
        );
    }
}
