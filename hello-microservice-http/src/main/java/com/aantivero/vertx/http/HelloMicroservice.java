package com.aantivero.vertx.http;

import io.vertx.core.AbstractVerticle;

public class HelloMicroservice extends AbstractVerticle {

    @Override
    public void start() {
        vertx.createHttpServer()
                .requestHandler(
                        req -> req.response().end("Hello"))
                .listen(8080);
    }

}
