package top.infsky.cheatdetector.impl.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class TimeTaskManager {
    private final @NotNull List<Queue<Runnable>> tasks;

    public TimeTaskManager() {
        this.tasks = new LinkedList<>();
    }

    public void addTask(Runnable task) {
        addTask(task, 0);
    }

    public void addTask(Runnable task, @Range(from = 0, to = Integer.MIN_VALUE) int delay) {
        try {
            tasks.get(delay).add(task);
        } catch (IndexOutOfBoundsException e) {
            if (delay > tasks.size())
                for (int i = tasks.size(); i <= delay; i++) {
                    tasks.add(new LinkedBlockingQueue<>());
                }
            tasks.add(delay, new LinkedBlockingQueue<>());
            addTask(task, delay);
        }
    }

    public void onTick() {
        try {
            for (Runnable task : tasks.remove(0)) {
                task.run();
            }
        } catch (IndexOutOfBoundsException ignored) {}
    }
}
