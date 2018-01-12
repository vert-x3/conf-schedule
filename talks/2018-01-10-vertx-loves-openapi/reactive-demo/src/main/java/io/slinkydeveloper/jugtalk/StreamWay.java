package io.slinkydeveloper.jugtalk;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * @author Francesco Guardiani @slinkydeveloper
 */
public class StreamWay {

    public static void main(String[] args) throws IOException {
        List<URL> urls = Utils.fillURLsList();

        int sum = urls
                .stream()
                .map(StreamWay::loadURL)
                .mapToInt(String::length)
                .reduce(0, Integer::sum);

        System.out.println("All loaded pages contains a number of character equals to " + sum + ". Completed at " + System.currentTimeMillis());

    }

    public static String loadURL(URL url) {
        String result;
        System.out.printf("Starting processing of %s at %d%n", url, System.currentTimeMillis());
        try {
            result = IOUtils.toString(url, "UTF-8");
        } catch (Exception e) {
            return "";
        }
        System.out.printf("Completed processing of %s at %d%n", url, System.currentTimeMillis());
        return result;
    }


}
