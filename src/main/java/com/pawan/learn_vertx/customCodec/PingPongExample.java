package com.pawan.learn_vertx.customCodec;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPongExample {
  private static final Logger logger = LoggerFactory.getLogger(PingPongExample.class);

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new PingVerticle(), logOnError());
    vertx.deployVerticle(new PongVerticle(), logOnError());
  }

  private static Handler<AsyncResult<String>> logOnError() {
    return ar -> {
      if (ar.failed()) {
        logger.error("err", ar.cause());
      }
    };
  }

  public static class PingVerticle extends AbstractVerticle{
    static final String ADDRESS = PingVerticle.class.getName();

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      var eventBus = vertx.eventBus();
      final  Ping message= new Ping("Hello",true);
      logger.debug("Sending: {}", message);
      //Register it only once
      eventBus.registerDefaultCodec(Ping.class, new LocalMessageCodec<>(Ping.class));
      vertx.eventBus().<Pong>request(ADDRESS, message, reply->{
        if (reply.failed()){
          logger.error("Failed: ", reply.cause());
        }
        logger.debug("Response: {}", reply.result().body());
      });
      startPromise.complete();
    }
  }

 public static class PongVerticle extends AbstractVerticle{
   @Override
   public void start(Promise<Void> startPromise) throws Exception {
     var eventBus = vertx.eventBus();
     eventBus.registerDefaultCodec(Pong.class, new LocalMessageCodec<>(Pong.class));
     vertx.eventBus().<Ping>consumer(PingVerticle.ADDRESS, message -> {
       logger.debug("Received message: {}", message.body());
       message.reply(new Pong(0));
     }).exceptionHandler(error->{
       logger.error("Error: ", error);
     });
   }
 }
}
