package io.slinkydeveloper.openapi.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.api.RequestParameters;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.ext.web.Router;
import io.vertx.core.Future;

public class HttpServerVerticle extends AbstractVerticle {

  HttpServer server;

  @Override
  public void start(Future future) {
    OpenAPI3RouterFactory.createRouterFactoryFromFile(this.vertx, getClass().getResource("/spec.yaml").getFile(), openAPI3RouterFactoryAsyncResult -> {
      if (openAPI3RouterFactoryAsyncResult.succeeded()) {
        OpenAPI3RouterFactory routerFactory = openAPI3RouterFactoryAsyncResult.result();

        // Enable automatic response when ValidationException is thrown
        routerFactory.enableValidationFailureHandler(true);

        // Add routes handlers
        routerFactory.addHandlerByOperationId("getTransactionsList", routingContext -> {
          vertx.eventBus().send("transactions.demo/list", null, messageAsyncResult -> {
            routingContext
                    .response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json")
                    .end(messageAsyncResult.result().body().toString());
          });
        });

        routerFactory.addHandlerByOperationId("putTransaction", routingContext -> {
            RequestParameters params = routingContext.get("parsedParameters");
            JsonObject obj = params.body().getJsonObject();
            vertx.eventBus().send("transactions.demo/add", obj);
            routingContext.response().setStatusCode(200).setStatusMessage("OK").end();
        });


        // Generate the router
        Router router = routerFactory.getRouter();
        server = vertx.createHttpServer(new HttpServerOptions().setPort(3000).setHost("localhost"));
        server.requestHandler(router::accept).listen(httpServerAsyncResult -> {
            if (httpServerAsyncResult.succeeded())
                System.out.println("Listening on port " + httpServerAsyncResult.result().actualPort());
            else
                System.out.println(httpServerAsyncResult.cause());
        });
        future.complete();
      } else {
          // Something went wrong during router factory initialization
          Throwable exception = openAPI3RouterFactoryAsyncResult.cause();
          System.err.println(exception);
          System.exit(1);
      }
    });
  }

  @Override
  public void stop(){
    this.server.close();
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new DataVerticle());
    vertx.deployVerticle(new HttpServerVerticle());
  }

}
