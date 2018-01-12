package io.slinkydeveloper.jugtalk;

import com.oracle.tools.packager.Log;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.EventBus;
import io.vertx.reactivex.ext.web.client.WebClient;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.Queue;

public class VertxWayWithBeautifulLogging extends AbstractVerticle {

    EventBus bus;
    Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Override
    public void start() throws IOException {
        List<URL> urls = Utils.fillURLsList();

        final WebClient client = WebClient.create(vertx);

        bus = vertx.eventBus();
        bus.consumer("started", message -> logger.info(MessageFormat.format("Starting processing of {0} at {1}", message.body(), System.currentTimeMillis())));
        bus.consumer("loaded", message -> logger.info(MessageFormat.format("Completed loading of {0} at {1}", message.body(), System.currentTimeMillis())));
        bus.consumer("finished", message -> logger.info(MessageFormat.format("All loaded pages contains a number of character equals to {0}. Completed at {1}", message.body(), System.currentTimeMillis())));

        Observable
                .fromIterable(urls)
                .flatMap(url -> {
                    bus.publish("started", url.toString());
                    return client.getAbs(url.toString()).rxSend().toObservable().map(bufferHttpResponse -> {
                        bus.publish("loaded", url.toString());
                        return bufferHttpResponse.bodyAsString();
                    });
                })
                .map(String::length)
                .reduce(0, (sum, string) -> sum + string)
                .subscribe(result -> bus.publish("finished", result));
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new VertxWayWithBeautifulLogging());
    }
}
