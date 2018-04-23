package com.simonjamesrowe.events.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

  @JsonProperty("list")
  private List<WeatherItem> items;

  public List<WeatherItem> getItems() {
    return items;
  }

  public void setItems(List<WeatherItem> items) {
    this.items = items;
  }
}
