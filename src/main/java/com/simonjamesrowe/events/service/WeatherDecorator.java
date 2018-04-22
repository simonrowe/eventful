package com.simonjamesrowe.events.service;

import com.simonjamesrowe.events.es.model.Event;
import com.simonjamesrowe.events.model.WeatherItem;
import com.simonjamesrowe.events.model.WeatherResponse;

public class WeatherDecorator {

  private WeatherResponse weatherResponse;
  private String weatherIconPattern;

  public WeatherDecorator(WeatherResponse weatherResponse, String weatherIconPattern) {
    this.weatherResponse = weatherResponse;
    this.weatherIconPattern = weatherIconPattern;
  }

  public Event decorate(Event source) {
    WeatherItem item = find(source);
    if (item != null) {
      source.setWeatherDescription(item.toString());
      source.setWeatherIcon(
          String.format(weatherIconPattern, item.getDescription().get(0).getIcon()));
    }
    return source;
  }

  private WeatherItem find(Event source) {
    if (weatherResponse == null) {
      return null;
    }
    int index = -1;
    long timeDifference = 999999999;
    for (int i = 0; i < weatherResponse.getItems().size(); i++) {
      long difference =
          Math.abs(
              source.getStartTime().getTime()
                  - weatherResponse.getItems().get(i).getStartTime().getMillis());
      if (difference < timeDifference) {
        index = i;
        timeDifference = difference;
      }
    }
    if (index == -1 || timeDifference > (3 * 60 * 60 * 1000)) {
      return null;
    }
    return weatherResponse.getItems().get(index);
  }
}
