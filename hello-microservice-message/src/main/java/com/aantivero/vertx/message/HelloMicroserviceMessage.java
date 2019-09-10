package com.aantivero.vertx.message;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

/**
 * Message Driven Verticle
 */
public class HelloMicroserviceMessage extends AbstractVerticle {

    @Override
    public void start() {
        // receive message from the addess 'hello'
        vertx.eventBus().<String>consumer("hello", message -> {
            JsonObject json = new JsonObject()
                    .put("served-by", this.toString());
            //check wheter we have received a payload in the incoming message
            if (message.body().isEmpty()){
                message.reply(json.put("message", "hello"));
            } else {
                message.reply(json.put("message", "hello " + message.body()));
            }
        });
    }

}
