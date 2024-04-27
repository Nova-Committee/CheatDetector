package top.infsky.cheatdetector.anticheat;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Module extends Fix {
    @Nullable
    public static Module instance = null;
    public Module(String moduleName, @NotNull TRSelf player) {
        super(moduleName, player);
        instance = this;
    }

    @Override
    public void flag() {
    }

    public void flag(String extraMsg) {
    }
}
