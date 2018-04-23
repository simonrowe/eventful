package com.simonjamesrowe.events.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URI;

public abstract class AbstractHttpDao<T> {

  protected ObjectMapper objectMapper;

  public AbstractHttpDao() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JodaModule());
  }

  protected T get(URI uri, Class clazz) throws IOException {
    String rawResponse = responseBody(uri);
    return (T) objectMapper.readValue(rawResponse, clazz);
  }

  protected String responseBody(URI uri) throws IOException {
    if (uri != null) {
      return IOUtils.toString(uri, "utf-8");
    } else {
      return null;
    }
  }
}
