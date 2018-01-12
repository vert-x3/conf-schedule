package io.slinkydeveloper.jugtalk;

import io.reactivex.Observable;
import io.vertx.core.Vertx;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class VertxRxWay extends AbstractVerticle {

    @Override
    public void start() throws IOException {
        List<URL> urls = Utils.fillURLsList();

        final WebClient client = WebClient.create(vertx);

        Observable
                .fromIterable(urls)
                .flatMap(url -> client.getAbs(url.toString()).rxSend().toObservable())
                .map(HttpResponse::bodyAsString)
                .map(String::length)
                .reduce(0, Integer::sum)
                .subscribe(result -> System.out.println("All loaded pages contains a number of character equals to " + result + ". Completed at " + System.currentTimeMillis()));
        System.out.println("Hello!");
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new VertxRxWay());
    }
}
