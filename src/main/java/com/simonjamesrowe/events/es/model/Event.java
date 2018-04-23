package com.simonjamesrowe.events.es.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties({"nameSort", "categorySort"})
@Document(indexName = "event", type = "event")
public class Event {

  @Id private String id;

  @Field(type = FieldType.keyword)
  private String location;

  @Field(type = FieldType.text)
  private String name;

  @Field(type = FieldType.keyword)
  private String nameSort;

  @Field(type = FieldType.text)
  private String description;

  @Field(type = FieldType.Date)
  private Date startTime;

  @Field(type = FieldType.keyword)
  private String venue;

  @Field(type = FieldType.keyword)
  private String categorySort;

  @Field(type = FieldType.keyword)
  private List<String> categories;

  @Field(type = FieldType.keyword)
  private List<String> categoryIds;

  @Field(type = FieldType.text)
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

  public String getCategorySort() {
    return categorySort;
  }

  public void setCategorySort(String categorySort) {
    this.categorySort = categorySort;
  }

  public String getNameSort() {
    return nameSort;
  }

  public void setNameSort(String nameSort) {
    this.nameSort = nameSort;
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
