package com.pawan.learn_vertx.customCodec;

public class Pong {

  private int id;

  public Pong(){

  }
  public Pong(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Pong{" +
      "id=" + id +
      '}';
  }
}
