package com.aantivero.vertx.http;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.web.*;
import io.vertx.rxjava.ext.web.client.*;
import io.vertx.rxjava.ext.web.codec.BodyCodec;
import rx.Single;

public class HelloConsumerMicroservice extends AbstractVerticle {

    private WebClient webClient;

    @Override
    public void start() {
        webClient = WebClient.create(vertx);

        Router router = Router.router(vertx);
        router.get("/").handler(this::invokeMyFirstMicroservice);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(9090);
    }

    private void invokeMyFirstMicroservice(RoutingContext routingContext) {
        HttpRequest<JsonObject> request1 = webClient
                .get(8080, "localhost", "/Luke")
                .as(BodyCodec.jsonObject());
        HttpRequest<JsonObject> request2 = webClient
                .get(8080, "localhost", "/Leia")
                .as(BodyCodec.jsonObject());

        Single<JsonObject> s1 = request1.rxSend().map(HttpResponse::body);
        Single<JsonObject> s2 = request2.rxSend().map(HttpResponse::body);

        Single
                .zip(s1, s2, (luke, leia) -> {
                    return new JsonObject()
                            .put("Luke", luke.getString("message"))
                            .put("Leia", leia.getString("message"));
                })
                .subscribe(
                        result -> routingContext.response().end(result.encodePrettily()),
                        error -> {
                            error.printStackTrace();
                            routingContext.response()
                                    .setStatusCode(500).end(error.getMessage());
                        }
                );
    }

}
