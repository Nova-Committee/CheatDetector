package top.infsky.cheatdetector.impl;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.utils.TRSelf;

public abstract class Module extends Fix {
    public Module(String moduleName, @NotNull TRSelf player) {
        super(moduleName, player);
    }

    @Override
    public void flag() {
    }

    @Override
    public void flag(String extraMsg) {
    }

    @Override
    public final int getAlertBuffer() {
        return super.getAlertBuffer();
    }
}
