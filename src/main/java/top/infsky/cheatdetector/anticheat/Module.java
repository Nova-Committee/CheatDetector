package top.infsky.cheatdetector.anticheat;

import org.jetbrains.annotations.NotNull;

public class Module extends Fix {
    public Module(String moduleName, @NotNull TRSelf player) {
        super(moduleName, player);
    }

    @Override
    public void flag() {
    }

    public void flag(String extraMsg) {
    }
}
