package com.pawan.learn_vertx.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WorkerExample extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(WorkerExample.class);

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new WorkerExample());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.deployVerticle(new WorkerVerticle(),
      new DeploymentOptions()
        .setWorker(true)
        .setWorkerPoolSize(1)
        .setWorkerPoolName("my-worker-verticle")
    );
    startPromise.complete();
    executeBlocking();
  }

  private void executeBlocking() {
    vertx.executeBlocking(event->{
      logger.debug("Executing the blocking code!");
      try {
        Thread.sleep(5000);
        event.complete("Thread sleep executed successfully!");
      } catch (Exception e){
        logger.error("Failed: "+e.getMessage());
        event.fail(e);
      }
      }, asyncResult -> {
      if(asyncResult.succeeded()){
        logger.debug("Done blocking the code!");
        System.out.println(asyncResult.result());
      }else {
        logger.debug("Blocking failed due to: ", asyncResult.result());
      }
    });
  }
}
