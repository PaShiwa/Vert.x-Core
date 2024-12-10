package com.pawan.learn_vertx;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(VertxExtension.class)
public class FuturePromiseExample {
  private static final Logger logger = LoggerFactory.getLogger(FuturePromiseExample.class);

  @Test
  void promise_success(Vertx vertx, VertxTestContext vertxTestContext){
     final Promise<String> promise= Promise.promise();
     logger.debug("Start");
     vertx.setTimer(500, id->{
       promise.complete("Success");
       logger.debug("Completed");
     });
     logger.debug("End");
    vertxTestContext.completeNow();
  }

  @Test
  void promise_failure(Vertx vertx, VertxTestContext vertxTestContext){
    final Promise<String> promise= Promise.promise();
    logger.debug("Start");
    vertx.setTimer(500, id->{
      promise.fail(new RuntimeException("Failed!"));
      logger.debug("Failed!");
    });
    logger.debug("End");
    vertxTestContext.completeNow();
  }

  @Test
  void future_success(Vertx vertx, VertxTestContext vertxTestContext){
    final Promise<String> promise= Promise.promise();
    logger.debug("Start");
    vertx.setTimer(500, id->{
      promise.complete("Success");
      logger.debug("Timer Done!");
    });
    final Future<String> future= promise.future();
    future.
      onSuccess(result->{
      logger.debug("Result: {}", result);
      vertxTestContext.completeNow();
    })
    .onFailure(vertxTestContext::failNow)
      ;
  }

  @Test
  void future_failure(Vertx vertx, VertxTestContext vertxTestContext){
    final Promise<String> promise= Promise.promise();
    logger.debug("Start");
    vertx.setTimer(500, id->{
      promise.fail(new RuntimeException("Failed!"));
      logger.debug("Timer Done!");
    });
    final Future<String> future= promise.future();
    future.
      onSuccess(result->{
        vertxTestContext.completeNow();
      })
      .onFailure(error->{
        logger.debug("Result: {}", error);
        vertxTestContext.completeNow();})
    ;
  }

  // Mapping future
  @Test
  void future_map(Vertx vertx, VertxTestContext vertxTestContext){
    final Promise<String> promise= Promise.promise();
    logger.debug("Start");
    vertx.setTimer(500, id->{
      promise.complete("Success");
      logger.debug("Timer Done!");
    });
    final Future<String> future= promise.future();
    future
      .map(asString-> {
        logger.debug("Map String to JsonObject");
        return new JsonObject().put("key", asString);
      })
      .map(asJsonObj-> new JsonArray().add(asJsonObj)) // Single line lambda expression ie. does not have curly braces. No return required.
      .onSuccess(result->{
        logger.debug("Result: {} of type {}", result, result.getClass().getName());
        vertxTestContext.completeNow();
      })
      .onFailure(vertxTestContext::failNow)
    ;
  }

  @Test
  void future_coordination(Vertx vertx, VertxTestContext vertxTestContext){
    vertx.createHttpServer()
      .requestHandler(httpServerRequest -> logger.debug("{}", httpServerRequest))
      .listen(10000)
      .compose(httpServer -> {
        logger.info("Another task!");
        return Future.succeededFuture(httpServer);
      })
      .compose(httpServer -> {
        logger.info("Even more!");
        return Future.succeededFuture(httpServer);
      })
      .onFailure(vertxTestContext::failNow)
      .onSuccess(server->{
        logger.debug("Server Starred on port {}",server.actualPort());
        vertxTestContext.completeNow();
      });
  }

  @Test
  void future_composition(Vertx vertx, VertxTestContext vertxTestContext){
    var one = Promise.<Void>promise();
    var two = Promise.promise();
    var three = Promise.promise();

    var futureOne = one.future();
    var futureTwo = one.future();
    var futureThree = one.future();

    vertx.setTimer(500, id->{
      one.complete();
      two.complete();
      three.complete();
    });

    CompositeFuture.all(futureOne,futureTwo, futureThree)
      .onFailure(vertxTestContext::failNow)
      .onSuccess(result->{
        logger.debug("Success");
        vertxTestContext.completeNow();
      });
  }
}
