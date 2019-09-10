package com.aantivero.vertx.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

public class HelloMicroservice extends AbstractVerticle {

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.get("/").handler(rc -> rc.response().end("Hello Router"));
        router.get("/:name").handler(rc -> rc.response()
        .end("Hola " + rc.pathParam("name")));

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080);
    }

}
