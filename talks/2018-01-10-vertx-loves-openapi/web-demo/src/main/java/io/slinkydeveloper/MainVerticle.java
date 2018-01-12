package io.slinkydeveloper;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

import java.text.MessageFormat;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() {
        Router router = Router.router(vertx);

        router.get("/api/hello")
                .handler(routingContext -> vertx.eventBus().send("pageloader", "", messageAsyncResult -> {
                    JsonObject object = (JsonObject) messageAsyncResult.result().body();
                    object.put("text", MessageFormat.format("Total characters: {0}. Computation time: {1}", object.getInteger("result"), object.getLong("time")));
                    routingContext.response()
                            .setStatusCode(200)
                            .putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(object));
                }));

        router.route().handler(StaticHandler.create());

        HttpServer server = vertx.createHttpServer(
                new HttpServerOptions()
                        .setPort(config().getInteger("http.port", 3000))
                        .setHost(config().getString("http.host", "localhost")));
        server.requestHandler(router::accept).listen();
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new DataProvider());
        vertx.deployVerticle(new MainVerticle());
    }
}
