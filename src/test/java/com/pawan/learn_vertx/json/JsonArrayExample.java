package com.pawan.learn_vertx.json;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonArrayExample {

  @Test
  void jsonArrayCanBeMapped(){
    final JsonArray myJsonArray = new JsonArray();
    myJsonArray
      .add(new JsonObject().put("id",1))
      .add(new JsonObject().put("id",2))
      .add(new JsonObject().put("id",3))
      .add("ransomValue") // JsonArray is flexible and can contain any kind of objects
      ;
    System.out.println(myJsonArray);
    assertEquals("[{\"id\":1},{\"id\":2},{\"id\":3},\"ransomValue\"]", myJsonArray.encode());
  }

}
