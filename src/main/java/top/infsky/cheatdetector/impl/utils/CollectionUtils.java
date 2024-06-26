package top.infsky.cheatdetector.impl.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CollectionUtils {
    public static <T> @NotNull Collection<T> getDifference(@NotNull Collection<T> list1, @NotNull Collection<T> list2) {
        List<T> difference = new ArrayList<>();
        Map<T, Integer> map = new HashMap<>();
        list1.forEach(e -> map.put(e, map.getOrDefault(e, 0) + 1));
        list2.forEach(e -> map.put(e, map.getOrDefault(e, 0) + 1));
        for (Map.Entry<T, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1)
                difference.add(entry.getKey());
        }
        return difference;
    }

    @Contract(pure = true)
    public static @NotNull String getSpilt(@NotNull Collection<?> list, String end) {
        if (list.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        for (Object item : list) {
            sb.append(item.toString());
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append(end);
        return sb.toString();
    }

    @Contract(pure = true)
    public static @NotNull String getSpilt(@NotNull Collection<?> list) {
        return getSpilt(list, "");
    }
}
