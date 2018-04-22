package com.simonjamesrowe.events.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.simonjamesrowe.events.model.EventResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;

@Repository
public class EventDaoImpl implements EventDao {

  private ObjectMapper objectMapper;

  public EventDaoImpl() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JodaModule());
  }

  @Override
  public EventResponse get(URI uri) throws IOException {
    String rawResponse = responseBody(uri);
    return objectMapper.readValue(rawResponse, EventResponse.class);
  }

  protected String responseBody(URI uri) throws IOException {
    if (uri != null) {
      return IOUtils.toString(uri, "utf-8");
    } else {
      return null;
    }
  }
}
