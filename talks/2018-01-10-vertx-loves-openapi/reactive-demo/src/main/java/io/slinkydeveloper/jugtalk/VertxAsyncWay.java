package io.slinkydeveloper.jugtalk;

import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class VertxAsyncWay extends AbstractVerticle {

    @Override
    public void start() throws IOException {
        List<URL> urls = Utils.fillURLsList();

        final WebClient client = WebClient.create(vertx);

        List<Future> futures = urls
                .stream()
                .map(url -> {
                    Future<HttpResponse<Buffer>> future = Future.future();
                    client.getAbs(url.toString()).send(future.completer());
                    return future;
                })
                .collect(Collectors.toList());

        CompositeFuture
                .all(futures)
                .setHandler(compositeFutureAsyncResult -> {
                    int result = compositeFutureAsyncResult.result().list()
                            .stream()
                            .map(obj -> (HttpResponse<Buffer>) obj)
                            .map(HttpResponse::bodyAsString)
                            .map(String::length)
                            .reduce(0, Integer::sum);
                    System.out.println("All loaded pages contains a number of character equals to " + result + ". Completed at " + System.currentTimeMillis());
                });

       }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new VertxAsyncWay());
    }
}
