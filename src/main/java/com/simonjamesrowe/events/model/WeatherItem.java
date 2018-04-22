package com.simonjamesrowe.events.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.WordUtils;
import org.joda.time.DateTime;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherItem {
  @JsonProperty("main")
  private WeatherMainItem mainItem;

  @JsonProperty("weather")
  private List<WeatherDescription> description;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonProperty("dt_txt")
  private DateTime startTime;

  public WeatherMainItem getMainItem() {
    return mainItem;
  }

  public void setMainItem(WeatherMainItem mainItem) {
    this.mainItem = mainItem;
  }

  public List<WeatherDescription> getDescription() {
    return description;
  }

  public void setDescription(List<WeatherDescription> description) {
    this.description = description;
  }

  public DateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(DateTime startTime) {
    this.startTime = startTime;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(mainItem.getMinTemp())
        .append("C - ")
        .append(mainItem.getMaxTemp())
        .append(" , Humidity: " + mainItem.getHumidity())
        .append(", ")
        .append(WordUtils.capitalizeFully(description.get(0).getDescription()));
    return sb.toString();
  }
}
