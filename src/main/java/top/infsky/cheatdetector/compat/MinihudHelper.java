package top.infsky.cheatdetector.compat;

import fi.dy.masa.minihud.util.DataStorage;

public class MinihudHelper {
    public static double getServerTPS() {
        return DataStorage.getInstance().getServerTPS();
    }
}
