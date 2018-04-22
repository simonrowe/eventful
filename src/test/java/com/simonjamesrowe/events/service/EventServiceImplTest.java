package com.simonjamesrowe.events.service;

import com.simonjamesrowe.events.converter.SourceEventConverter;
import com.simonjamesrowe.events.dao.EventDao;
import com.simonjamesrowe.events.es.dao.ElasticSearchEventDao;
import com.simonjamesrowe.events.es.model.Event;
import com.simonjamesrowe.events.model.EventData;
import com.simonjamesrowe.events.model.EventResponse;
import com.simonjamesrowe.events.model.SourceEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EventServiceImplTest {

  @Mock private EventDao eventDao;

  @Mock private ElasticSearchEventDao elasticSearchEventDao;

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
  }

  @Test
  public void testLoadIntoElasticSearch() throws Exception {
    URI countUri =
        new URI(
            "http://api.eventful.com/json/events/search?app_key=blah123&location=London&include=categories&count_only=true");
    EventResponse countResponse = countResponse();
    when(eventDao.get(eq(countUri))).thenReturn(countResponse);

    URI pageOneUrl =
        new URI(
            "http://api.eventful.com/json/events/search?app_key=blah123&location=London&include=categories&page_size=2&page_number=1");
    when(eventDao.get(eq(pageOneUrl))).thenReturn(pageResponse(1, 2));

    URI pageTwoUrl =
        new URI(
            "http://api.eventful.com/json/events/search?app_key=blah123&location=London&include=categories&page_size=2&page_number=2");
    when(eventDao.get(eq(pageTwoUrl))).thenReturn(pageResponse(2, 1));
    ArgumentCaptor<List<Event>> esParamCapture = ArgumentCaptor.forClass(List.class);

    eventServiceImpl.loadIntoElasticSearch();

    verify(elasticSearchEventDao, times(2)).saveAll(esParamCapture.capture());

    assertEquals("1_1", esParamCapture.getAllValues().get(0).get(0).getId());
    assertEquals("1_2", esParamCapture.getAllValues().get(0).get(1).getId());
    assertEquals(2, esParamCapture.getAllValues().get(0).size());
    assertEquals("2_1", esParamCapture.getAllValues().get(1).get(0).getId());
    assertEquals(1, esParamCapture.getAllValues().get(1).size());
  }

  private EventResponse pageResponse(int pageNumber, int pageSize) {
    EventResponse eventResponse = new EventResponse();
    EventData eventData = new EventData();
    eventResponse.setEventData(eventData);
    eventData.setEvents(new ArrayList<>());
    for (int i = 1; i <= pageSize; i++) {
      SourceEvent se = new SourceEvent();
      se.setId(String.valueOf(pageNumber) + "_" + i);
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
