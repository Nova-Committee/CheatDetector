package top.infsky.cheatdetector.utils;

import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.infsky.cheatdetector.CheatDetector;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

public class LogUtils {
    public static final Logger LOGGER = LoggerFactory.getLogger("CheatDetector");

    public static void alert(String player, String module, String extraMsg) {
        if (check()) {
            assert CheatDetector.CLIENT.player != null;
            CheatDetector.CLIENT.player.sendSystemMessage(Component.literal(
                    String.format("§b§lTR§r§l> §r%s§r %s§r %s§r | %s§r", player, Component.translatable("chat.cheatdetector.alert.fail").getString(), module, extraMsg)
            ));
        }
    }

    public static void prefix(String prefix, String msg) {
        if (check()) {
            assert CheatDetector.CLIENT.player != null;
            CheatDetector.CLIENT.player.sendSystemMessage(Component.literal(
                    String.format("§b§lTR§r§l> §r%s§r | %s§r", prefix, msg)
            ));
        }
    }

    public static void custom(String msg) {
        if (check()) {
            assert CheatDetector.CLIENT.player != null;
            CheatDetector.CLIENT.player.sendSystemMessage(Component.literal(
                    String.format("§b§lTR§r§l> §r%s§r", msg)
            ));
        }


    }

    private static boolean check() {
        return CONFIG().getAlert().isAllowAlert() && CheatDetector.CLIENT.player != null;
    }
}
