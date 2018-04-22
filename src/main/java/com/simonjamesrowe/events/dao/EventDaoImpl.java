package com.simonjamesrowe.events.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.simonjamesrowe.events.model.EventResponse;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;

@Repository
public class EventDaoImpl extends AbstractHttpDao<EventResponse> implements EventDao {

  private ObjectMapper objectMapper;

  public EventDaoImpl() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JodaModule());
  }

  @Override
  public EventResponse get(URI uri) throws IOException {
    return super.get(uri, EventResponse.class);
  }
}
