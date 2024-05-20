package top.infsky.cheatdetector.impl.utils.packet;

import org.jetbrains.annotations.Nullable;
import top.infsky.cheatdetector.utils.LogUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class NetUtils {
    public static @Nullable String get(String url, String charsetName) {
        StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader;

        try {
            URLConnection connection = (new URL(url)).openConnection();
            connection.connect();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charsetName));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }

            bufferedReader.close();
            return result.toString();
        } catch (IOException e) {
            LogUtils.custom("Exception in console.");
            LogUtils.LOGGER.error("在网络请求时遇到异常: %s", e);
        }

        return null;
    }
}
