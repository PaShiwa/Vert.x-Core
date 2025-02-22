package com.pawan.learn_vertx.json;

import com.fasterxml.jackson.core.JsonToken;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonObjectExample {
  @Test
  void jsonObjectCanBeMapped() {
    final JsonObject myJsonObject = new JsonObject();
    myJsonObject.put("id", 1);
    myJsonObject.put("name", "Pawan");
    myJsonObject.put("loves_vertx", true);

    final String encoded = myJsonObject.encode();
    assertEquals("{\"id\":1,\"name\":\"Pawan\",\"loves_vertx\":true}", encoded);

    final JsonObject decodedJsonObject = new JsonObject(encoded);
    assertEquals(myJsonObject,decodedJsonObject);
  }

  @Test
  void jsonObjectCanBeCreatedFromMap(){
    final Map<String,Object> myMap = new HashMap<>();
    myMap.put("id", 1);
    myMap.put("name", "Pawan");
    myMap.put("loves_vertx", true);
    final JsonObject asJsonObject= new JsonObject(myMap);
    assertEquals(myMap,asJsonObject.getMap());
    assertEquals(1,asJsonObject.getInteger("id"));
    assertEquals("Pawan",asJsonObject.getString("name"));
    assertEquals(true,asJsonObject.getBoolean("loves_vertx"));
  }
}
