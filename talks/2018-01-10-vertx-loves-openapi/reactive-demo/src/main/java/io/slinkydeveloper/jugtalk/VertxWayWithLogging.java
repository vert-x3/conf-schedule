package io.slinkydeveloper.jugtalk;

import io.reactivex.Observable;
import io.vertx.core.Vertx;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.client.WebClient;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class VertxWayWithLogging extends AbstractVerticle {

    @Override
    public void start() throws IOException {
        List<URL> urls = Utils.fillURLsList();

        final WebClient client = WebClient.create(vertx);

        Observable
                .fromIterable(urls)
                .flatMap(url -> {
                    System.out.printf("Starting processing of %s at %d%n", url, System.currentTimeMillis());
                    return client.getAbs(url.toString()).rxSend().toObservable().map(bufferHttpResponse -> {
                        System.out.printf("Completed loading of %s at %d%n", url, System.currentTimeMillis());
                        return bufferHttpResponse.bodyAsString();
                    });
                })
                .map(String::length)
                .reduce(0, (sum, string) -> sum + string)
                .subscribe(result -> {
                    System.out.println("All loaded pages contains a number of character equals to " + result + ". Completed at " + System.currentTimeMillis());
                });
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new VertxWayWithLogging());
    }
}
