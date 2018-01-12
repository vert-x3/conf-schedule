package io.slinkydeveloper.jugtalk;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * @author Francesco Guardiani @slinkydeveloper
 */
public class ClassicWay {

    public static void main(String[] args) throws IOException {
        List<URL> urls = Utils.fillURLsList();

        int count = 0;

        for (URL url : urls) {
            System.out.printf("Starting processing of %s at %d%n", url, System.currentTimeMillis());
            count += IOUtils.toString(url, "UTF-8").length();
            System.out.printf("Completed processing of %s at %d%n", url, System.currentTimeMillis());
        }

        System.out.println("All loaded pages contains a number of character equals to " + count + ". Completed at " + System.currentTimeMillis());

    }

}
