package io.slinkydeveloper.openapi.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author Francesco Guardiani @slinkydeveloper
 */
public class DataVerticle extends AbstractVerticle {

    DataProvider dataProvider;

    @Override
    public void start() {
        dataProvider = new DataProvider();
        EventBus eventBus = vertx.eventBus();

        eventBus.consumer("transactions.demo/add").handler(objectMessage -> {
            JsonObject json = (JsonObject) objectMessage.body();
            dataProvider.addTransaction(json.mapTo(Transaction.class));
        });

        eventBus.consumer("transactions.demo/list").handler(objectMessage -> {
            objectMessage.reply(Json.encode(dataProvider.getTransactions()));
        });


    }

}
