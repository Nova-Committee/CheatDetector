package top.infsky.cheatdetector.utils;

import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.config.AlertConfig;

public class LogUtils {
    public static final Logger LOGGER = LoggerFactory.getLogger("CheatDetector");

    public static void alert(String player, String module, String extraMsg) {
        if (check()) {
            CheatDetector.CLIENT.player.sendSystemMessage(Component.literal(
                    String.format("%s§r §r%s§r %s§r %s§r | %s§r", AlertConfig.prefix, player, Component.translatable("cheatdetector.chat.alert.fail").getString(), module, extraMsg)
            ));
        }
    }

    public static void prefix(String prefix, String msg) {
        if (check()) {
            CheatDetector.CLIENT.player.sendSystemMessage(Component.literal(
                    String.format("%s§r §r%s§r | %s§r", AlertConfig.prefix, prefix, msg)
            ));
        }
    }

    public static void custom(String msg) {
        if (check()) {
            CheatDetector.CLIENT.player.sendSystemMessage(Component.literal(
                    String.format("%s§r §r%s§r", AlertConfig.prefix, msg)
            ));
        }


    }

    private static boolean check() {
        return AlertConfig.allowAlert && CheatDetector.CLIENT.player != null;
    }
}
