package io.slinkydeveloper;

import io.reactivex.Observable;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.eventbus.EventBus;
import io.vertx.reactivex.core.eventbus.Message;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;

import java.util.List;

public class DataProvider extends AbstractVerticle {

    class PagesLoader implements Handler<Message<Object>> {

        private final List<String> urls;
        private final WebClient client;

        PagesLoader(List<String> urls, Vertx vertx) {
            this.urls = urls;
            this.client = WebClient.create(vertx);
        }


        @Override
        public void handle(Message message) {
            long startTime = System.currentTimeMillis();
            Observable
                    .fromIterable(urls)
                    .flatMap(url -> client.getAbs(url).rxSend().toObservable())
                    .map(HttpResponse::bodyAsString)
                    .map(String::length)
                    .reduce(0, Integer::sum)
                    .subscribe(result ->
                        message.reply(new JsonObject().put("result", result).put("time", (System.currentTimeMillis() - startTime)))
                    );
        }
    }

    @Override
    public void start() {
        EventBus eb = vertx.eventBus();

        List<String> urls = List.of(
                "https://www.google.it/",
                "https://slinkydeveloper.github.io",
                "https://twitter.com",
                "https://facebook.com",
                "https://mvnrepository.com"
        );

        eb.consumer("pageloader").handler(new PagesLoader(urls, vertx));
    }
}
