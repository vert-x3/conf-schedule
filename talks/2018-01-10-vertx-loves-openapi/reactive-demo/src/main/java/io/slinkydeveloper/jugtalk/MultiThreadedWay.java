package io.slinkydeveloper.jugtalk;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Francesco Guardiani @slinkydeveloper
 */
public class MultiThreadedWay {

    public static void main(String[] args) throws IOException, InterruptedException {
        List<URL> urls = Utils.fillURLsList();
        List<Thread> threads = new ArrayList<>();

        final AtomicInteger count = new AtomicInteger(0);

        for (URL url : urls) {
            Thread t = new Thread(() -> {
                System.out.printf("Starting processing of %s at %d%n", url, System.currentTimeMillis());
                try {
                    count.addAndGet(IOUtils.toString(url, "UTF-8").length());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.printf("Completed processing of %s at %d%n", url, System.currentTimeMillis());
            });
            t.start();
            threads.add(t);
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("All loaded pages contains a number of character equals to " + count + ". Completed at " + System.currentTimeMillis());

    }

}
