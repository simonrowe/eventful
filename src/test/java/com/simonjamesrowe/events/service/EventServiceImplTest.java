package com.simonjamesrowe.events.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.simonjamesrowe.events.converter.SourceEventConverter;
import com.simonjamesrowe.events.dao.EventDao;
import com.simonjamesrowe.events.dao.WeatherDao;
import com.simonjamesrowe.events.es.dao.ElasticSearchEventDao;
import com.simonjamesrowe.events.es.model.Event;
import com.simonjamesrowe.events.model.EventData;
import com.simonjamesrowe.events.model.EventResponse;
import com.simonjamesrowe.events.model.SourceEvent;
import com.simonjamesrowe.events.model.WeatherResponse;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EventServiceImplTest {

  @Mock private EventDao eventDao;

  @Mock private ElasticSearchEventDao elasticSearchEventDao;

  @Mock private WeatherDao weatherDao;

  private SourceEventConverter sourceEventConverter;

  @InjectMocks private EventServiceImpl eventServiceImpl;

  @Before
  public void setup() {
    ReflectionTestUtils.setField(eventServiceImpl, "pageSize", 2);
    ReflectionTestUtils.setField(
        eventServiceImpl, "sourceEventConverter", new SourceEventConverter());
    ReflectionTestUtils.setField(eventServiceImpl, "apiKey", "blah123");
    ReflectionTestUtils.setField(
        eventServiceImpl, "eventsRestEndpoint", "http://api.eventful.com/json/events/search");
    ReflectionTestUtils.setField(eventServiceImpl, "location", "London");
    ReflectionTestUtils.setField(eventServiceImpl, "include", "categories");
    ReflectionTestUtils.setField(
        eventServiceImpl,
        "weatherRestEndpoint",
        "http://api.openweathermap.org/data/2.5/forecast?q=London,gb&mode=json&APPID={appid}&units=metric");
    ReflectionTestUtils.setField(eventServiceImpl, "weatherApiKey", "changeMe");
    ReflectionTestUtils.setField(
        eventServiceImpl, "weatherIconUrl", "http://openweathermap.org/img/w/%s.png");
  }

  @Test
  public void testLoadIntoElasticSearch() throws Exception {
    eventExpectations();

    ArgumentCaptor<List<Event>> esParamCapture = ArgumentCaptor.forClass(List.class);

    eventServiceImpl.loadIntoElasticSearch();

    verify(elasticSearchEventDao, times(2)).saveAll(esParamCapture.capture());

    assertEquals("1_1", esParamCapture.getAllValues().get(0).get(0).getId());
    assertEquals("1_2", esParamCapture.getAllValues().get(0).get(1).getId());
    assertEquals(2, esParamCapture.getAllValues().get(0).size());
    assertEquals("2_1", esParamCapture.getAllValues().get(1).get(0).getId());
    assertEquals(1, esParamCapture.getAllValues().get(1).size());
  }

  private WeatherResponse sampleWeatherData() throws IOException {
    String responseBody =
        IOUtils.toString(new ClassPathResource("weather.json").getInputStream(), "utf-8");
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JodaModule());
    return objectMapper.readValue(responseBody, WeatherResponse.class);
  }

  @Test
  public void testLoadIntoElasticSearchWitWeatherData() throws Exception {
    eventExpectations();
    given(
            weatherDao.get(
                eq(
                    new URI(
                        "http://api.openweathermap.org/data/2.5/forecast?q=London,gb&mode=json&APPID=changeMe&units=metric"))))
        .willReturn(sampleWeatherData());
    ArgumentCaptor<List<Event>> esParamCapture = ArgumentCaptor.forClass(List.class);

    eventServiceImpl.loadIntoElasticSearch();

    verify(elasticSearchEventDao, times(2)).saveAll(esParamCapture.capture());

    assertEquals("1_1", esParamCapture.getAllValues().get(0).get(0).getId());
    assertEquals(
        "10.44C - 10.44 , Humidity: 73, Light Rain",
        esParamCapture.getAllValues().get(0).get(0).getWeatherDescription());
    assertEquals(
        "http://openweathermap.org/img/w/10d.png",
        esParamCapture.getAllValues().get(0).get(0).getWeatherIcon());
    assertEquals("1_2", esParamCapture.getAllValues().get(0).get(1).getId());
    assertNull(esParamCapture.getAllValues().get(0).get(1).getWeatherDescription());
    assertNull(esParamCapture.getAllValues().get(0).get(1).getWeatherIcon());
    assertEquals(2, esParamCapture.getAllValues().get(0).size());
    assertEquals("2_1", esParamCapture.getAllValues().get(1).get(0).getId());
    assertNull(esParamCapture.getAllValues().get(1).get(0).getWeatherDescription());
    assertNull(esParamCapture.getAllValues().get(1).get(0).getWeatherIcon());
    assertEquals(1, esParamCapture.getAllValues().get(1).size());
  }

  private void eventExpectations() throws URISyntaxException, IOException {
    URI countUri =
        new URI(
            "http://api.eventful.com/json/events/search?app_key=blah123&location=London&include=categories&sort_order=date&count_only=true");
    EventResponse countResponse = countResponse();
    when(eventDao.get(eq(countUri))).thenReturn(countResponse);
    URI pageOneUrl =
        new URI(
            "http://api.eventful.com/json/events/search?app_key=blah123&location=London&include=categories&sort_order=date&page_size=2&page_number=1");
    when(eventDao.get(eq(pageOneUrl))).thenReturn(pageResponse(1, 2));
    URI pageTwoUrl =
        new URI(
            "http://api.eventful.com/json/events/search?app_key=blah123&location=London&include=categories&sort_order=date&page_size=2&page_number=2");
    when(eventDao.get(eq(pageTwoUrl))).thenReturn(pageResponse(2, 1));
  }

  private EventResponse pageResponse(int pageNumber, int pageSize) {
    EventResponse eventResponse = new EventResponse();
    EventData eventData = new EventData();
    eventResponse.setEventData(eventData);
    eventData.setEvents(new ArrayList<>());
    for (int i = 1; i <= pageSize; i++) {
      SourceEvent se = new SourceEvent();
      se.setId(String.valueOf(pageNumber) + "_" + i);
      se.setStartTime(
          new DateTime()
              .withYear(2018)
              .withMonthOfYear(4)
              .withDayOfMonth(22)
              .withHourOfDay(10)
              .withMinuteOfHour(0)
              .withSecondOfMinute(0)
              .plusDays(i + pageNumber + 3));
      eventData.getEvents().add(se);
    }
    return eventResponse;
  }

  private EventResponse countResponse() {
    EventResponse countResponse = new EventResponse();
    countResponse.setSize(3l);
    return countResponse;
  }
}
