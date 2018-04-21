package com.simonjamesrowe.events.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.simonjamesrowe.events.domain.EventResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.UriBuilder;

import java.io.IOException;

@Repository
public class EventDao {

  private ObjectMapper objectMapper;

  public EventDao() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JodaModule());
  }

  @Value("${events.api.key}")
  private String apiKey;

  @Autowired private UriBuilder uriBuilder;

  public EventResponse findAllEvents() throws Exception {
    String rawResponse =
        responseBody("app_key={}&location={}&include={}", apiKey, "London", "categories");
    return objectMapper.readValue(rawResponse, EventResponse.class);
  }

  protected String responseBody(String uriTemplate, String... arguments) throws IOException {
    if (StringUtils.isNotEmpty(uriTemplate)) {
      return IOUtils.toString(uriBuilder.fragment(uriTemplate).build(arguments), "utf-8");
    } else {
      return null;
    }
  }
}
