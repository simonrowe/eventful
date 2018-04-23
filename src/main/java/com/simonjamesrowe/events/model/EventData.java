package com.simonjamesrowe.events.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventData {

  @JsonProperty("event")
  private List<SourceEvent> events;

  public List<SourceEvent> getEvents() {
    return events;
  }

  public void setEvents(List<SourceEvent> events) {
    this.events = events;
  }
}
