package com.simonjamesrowe.events.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherMainItem {

  @JsonProperty("temp_min")
  private BigDecimal minTemp;

  @JsonProperty("temp_max")
  private BigDecimal maxTemp;

  @JsonProperty("humidity")
  private Integer humidity;

  public BigDecimal getMinTemp() {
    return minTemp;
  }

  public void setMinTemp(BigDecimal minTemp) {
    this.minTemp = minTemp;
  }

  public BigDecimal getMaxTemp() {
    return maxTemp;
  }

  public void setMaxTemp(BigDecimal maxTemp) {
    this.maxTemp = maxTemp;
  }

  public Integer getHumidity() {
    return humidity;
  }

  public void setHumidity(Integer humidity) {
    this.humidity = humidity;
  }
}
