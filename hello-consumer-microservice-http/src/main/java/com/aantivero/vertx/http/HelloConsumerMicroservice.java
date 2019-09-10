package com.aantivero.vertx.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;

public class HelloConsumerMicroservice extends AbstractVerticle {

    private WebClient webClient;

    @Override
    public void start() {
        webClient = WebClient.create(vertx);

        Router router = Router.router(vertx);
        router.get("/").handler(this::invokeMyFirstMicroservice);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8081);
    }

    private void invokeMyFirstMicroservice(RoutingContext routingContext) {
        HttpRequest<JsonObject> request = webClient
                .get(8080, "localhost", "/vert.x")
                .as(BodyCodec.jsonObject());

        request.send(ar -> {
            if (ar.failed()){
                routingContext.fail(ar.cause());
            } else {
                routingContext.response()
                        .end(ar.result().body().encode());
            }
        });
    }

}
