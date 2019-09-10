package com.aantivero.vertx.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class HelloMicroservice extends AbstractVerticle {

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.get("/").handler(rc -> rc.response().end("Hello Router"));
        router.get("/:name").handler(this::hello);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080);
    }

    private void hello(RoutingContext routingContext) {
        String message = "Holis";
        if (routingContext.pathParam("name") != null) {
            message += " " + routingContext.pathParam("name");
        }
        JsonObject jsonObject = new JsonObject().put("message", message);
        routingContext.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .end(jsonObject.encode());
    }

}
