package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import java.util.logging.Logger;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    // Create a router
    Router router = Router.router(vertx);

    // Mount the handler for all incoming requests at every path and HTTP Method
    router.route().handler(context -> {
      // Get the address
      String address = context.request().connection().remoteAddress().toString();
      // Get the query param "name"
      MultiMap queryParams = context.queryParams();
      String name = queryParams.contains("name") ? queryParams.get("name") : "unknown";
      //Write a json response
      context.json(
        new JsonObject()
          .put("name", name)
          .put("address", address)
          .put("message", "Hello " + name + " connected from " + address)
      );
    });

    // Create an HTTP server
    vertx.createHttpServer()
      // Handle every request using the router
      .requestHandler(router)
      // Start the server
      .listen(8888)
      // print the port
      .onSuccess(httpServer -> {
        System.out.println("HTTP server started on port " + httpServer.actualPort());
      });

  }
}
