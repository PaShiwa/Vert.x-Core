package com.pawan.learn_vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponseExampleJSON {
  private static final Logger logger = LoggerFactory.getLogger(RequestResponseExampleJSON.class);

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new RequestVerticle());
    vertx.deployVerticle(new ResponseVerticle());
  }

  static class RequestVerticle extends AbstractVerticle{
    static final String ADDRESS = "RequestResponseExample";

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      final  var message= new JsonObject()
        .put("message","Hello World!")
        .put("version",1);
      logger.debug("Sending: {}", message);
      vertx.eventBus().<JsonArray>request(ADDRESS, message, reply->{
        logger.debug("Response: {}", reply.result().body());
      });
    }
  }

 static class ResponseVerticle extends AbstractVerticle{
   @Override
   public void start(Promise<Void> startPromise) throws Exception {
     vertx.eventBus().<JsonObject>consumer(RequestVerticle.ADDRESS, message -> {
       logger.debug("Received message: {}", message.body());
       message.reply(new JsonArray()
         .add("one")
         .add("two")
         .add("three"));
     });
   }
 }
}
