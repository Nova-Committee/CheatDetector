package top.infsky.cheatdetector.impl.modules.common;

import lombok.Getter;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.config.ModuleConfig;

public class SayHacker extends Module {
    @Getter
    @Nullable
    private static Module instance = null;
    public SayHacker(@NotNull TRSelf player) {
        super("SayHacker", player);
        instance = this;
    }

    public void _onFailed(String hacker, String module, String extraMsg) {
        if (isDisabled()) return;
        player.fabricPlayer.connection.sendChat(Component.translatable("cheatdetector.chat.say.sayHacker").getString()
                .formatted(fix(hacker), fix(module), fix(extraMsg)));
    }

    public String fix(@NotNull String string) {
        StringBuilder builder = new StringBuilder();
        boolean ignoredNext = false;
        for (char part : string.toCharArray()) {
            if (ignoredNext) {
                ignoredNext = false;
                continue;
            }
            if (part == '§') {
                ignoredNext = true;
                continue;
            }
            builder.append(part);
        }
        return builder.toString();
    }
    @Override
    public boolean isDisabled() {
        return !ModuleConfig.sayHackerEnabled;
    }
}
