package com.pawan.learn_vertx.json;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MappingJavaObjectsExample {

  @Test
  void canMapJavaObjects(){
    final Person person1= new Person(1, "Pawan", true);
    final JsonObject pawan = JsonObject.mapFrom(person1);

    assertEquals(person1.getId(),pawan.getInteger("id"));
    assertEquals(person1.getName(),pawan.getString("name"));
    assertEquals(person1.isLovesVertx(),pawan.getBoolean("lovesVertx"));

    final Person person2 = pawan.mapTo(Person.class);
    assertEquals(person1.getId(), person2.getId());
    assertEquals(person1.getName(), person2.getName());
    assertEquals(person1.isLovesVertx(), person2.isLovesVertx());
  }
}
