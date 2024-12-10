package com.pawan.learn_vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponseExample {
  private static final Logger logger = LoggerFactory.getLogger(RequestResponseExample.class);

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new RequestVerticle());
    vertx.deployVerticle(new ResponseVerticle());
  }

  static class RequestVerticle extends AbstractVerticle{
    static final String ADDRESS = "RequestResponseExample";

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      final String message= "Hello World!";
      logger.debug("Sending: {}", message);
      vertx.eventBus().<String>request(ADDRESS, message, reply->{
        logger.debug("Response: {}", reply.result().body());
      });
    }
  }

 static class ResponseVerticle extends AbstractVerticle{
   @Override
   public void start(Promise<Void> startPromise) throws Exception {
     vertx.eventBus().<String>consumer(RequestVerticle.ADDRESS, message -> {
       logger.debug("Received message: {}", message.body());
       message.reply("Recived your message. Thanks!");
     });
   }
 }
}
