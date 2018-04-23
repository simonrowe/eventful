package com.simonjamesrowe.events.es.model;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Date;
import java.util.List;

@Document(indexName = "event", type = "event")
public class Event {

  @Id private String id;

  @Field(type = FieldType.keyword)
  private String location;

  @MultiField(
    mainField = @Field(type = FieldType.text),
    otherFields = {@InnerField(suffix = "sort", type = FieldType.keyword)}
  )
  private String name;

  @Field(type = FieldType.text)
  private String description;

  @Field(type = FieldType.Date)
  private Date startTime;

  @MultiField(
    mainField = @Field(type = FieldType.text),
    otherFields = {@InnerField(suffix = "sort", type = FieldType.keyword)}
  )
  private String venue;

  @MultiField(
    mainField = @Field(type = FieldType.text),
    otherFields = {@InnerField(suffix = "sort", type = FieldType.keyword)}
  )
  private List<String> categories;

  @Field(type = FieldType.keyword)
  private List<String> categoryIds;

  @MultiField(
    mainField = @Field(type = FieldType.text),
    otherFields = {@InnerField(suffix = "sort", type = FieldType.keyword)}
  )
  private String weatherDescription;

  @Field(type = FieldType.keyword)
  private String weatherIcon;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public String getVenue() {
    return venue;
  }

  public void setVenue(String venue) {
    this.venue = venue;
  }

  public List<String> getCategories() {
    return categories;
  }

  public void setCategories(List<String> categories) {
    this.categories = categories;
  }

  public List<String> getCategoryIds() {
    return categoryIds;
  }

  public void setCategoryIds(List<String> categoryIds) {
    this.categoryIds = categoryIds;
  }

  public String getWeatherDescription() {
    return weatherDescription;
  }

  public void setWeatherDescription(String weatherDescription) {
    this.weatherDescription = weatherDescription;
  }

  public String getWeatherIcon() {
    return weatherIcon;
  }

  public void setWeatherIcon(String weatherIcon) {
    this.weatherIcon = weatherIcon;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }
}
