package com.pawan.learn_vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PointToPointExample {
  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new Sender());
    vertx.deployVerticle(new Receiver());
  }

  static class Sender extends AbstractVerticle{
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      // Sending the message every second!
      vertx.setPeriodic(1000,id->{
        vertx.eventBus().send(Sender.class.getName(),"Sending a message.....");
      });
    }
  }

  static class Receiver extends AbstractVerticle{
    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(Sender.class.getName(),message -> {
      logger.debug("Received: {}", message.body());
      });
    }
  }
}
