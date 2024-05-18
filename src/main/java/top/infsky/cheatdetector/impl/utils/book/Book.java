package top.infsky.cheatdetector.impl.utils.book;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface Book {
    @NotNull String getTitle();
    @NotNull List<String> getString();

    default @NotNull List<String> getBook() {
        List<String> result = new ArrayList<>();
        List<String> currentPage = new ArrayList<>(14);
        StringBuilder currentLine = new StringBuilder();

        for (String page : getString()) {
            for (String line : page.split("\n")) {
                for (String s : line.split("")) {
                    if (currentPage.size() == 14) {
                        StringBuilder pageStr = new StringBuilder();
                        currentPage.forEach(string -> pageStr.append(string).append("\n"));
                        result.add(pageStr.toString());
                        currentPage.clear();
                    }
                    if (currentLine.length() == 12) {
                        currentPage.add(currentLine.toString());
                        currentLine = new StringBuilder();
                    }

                    currentLine.append(s);
                }

                if (!currentLine.isEmpty() && currentPage.size() < 14) {  // 如果刚刚没有换行且可以换行
                    currentPage.add(currentLine.toString());
                    currentLine = new StringBuilder();
                }
            }
            if (!currentPage.isEmpty()) {  // 如果可以换页
                StringBuilder pageStr = new StringBuilder();
                currentPage.forEach(string -> pageStr.append(string).append("\n"));
                result.add(pageStr.toString());
                currentPage.clear();
            }
        }
        return result;
    }
}
