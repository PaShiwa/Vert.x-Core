package com.pawan.learn_vertx.customCodec;

import com.fasterxml.jackson.databind.DeserializationConfig;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class LocalMessageCodec<T> implements MessageCodec<T,T> {

  private final String typename;

  public LocalMessageCodec(Class<T> type) {
    this.typename = type.getName();
  }

  @Override
  // Serialization
  public void encodeToWire(final Buffer buffer, final T t) {
    throw new UnsupportedOperationException("Only local encode is supported!");
  }

  @Override
  //Deserialization
  public T decodeFromWire(final int pos, final Buffer buffer) {
    throw new UnsupportedOperationException("Only local encode is supported!");

  }

  //In the local mode, within the same JVM, the Event Bus does not need to serialize or deserialize the objects
  @Override
  public T transform(final T obj) {
    return obj;
  }

  @Override
  public String name() {
    return this.typename;
  }

  @Override
  public byte systemCodecID() {
    return -1;
  }
}
