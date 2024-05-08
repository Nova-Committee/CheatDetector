package top.infsky.cheatdetector.impl.utils;

public class ClickSimulator {
    private double minDelay;
    private double maxDelay;
    private int hasDelay;

    public ClickSimulator(double minCPS, double maxCPS) {
        setCPS(minCPS, maxCPS);
        hasDelay = 0;
    }

    public void setCPS(double minCPS, double maxCPS) {
        minDelay = 20 / maxCPS;
        maxDelay = 20 / minCPS;
    }

    public boolean tickShouldClick() {
        hasDelay++;
        if (hasDelay < minDelay) return false;
        if (hasDelay >= maxDelay) {
            hasDelay = 0;
            return true;
        }

        double currentDelay = Math.random() * (maxDelay - minDelay) + minDelay;

        if (hasDelay >= currentDelay) {
            hasDelay = 0;
            return true;
        }
        return false;
    }
}
