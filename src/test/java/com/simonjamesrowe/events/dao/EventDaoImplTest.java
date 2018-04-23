package com.simonjamesrowe.events.dao;

import com.simonjamesrowe.events.model.EventResponse;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.util.UriBuilder;

import java.net.URI;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class EventDaoImplTest {

  private static final String TEST_API_KEY = "12346578";

  @InjectMocks private EventDaoImpl eventDao;

  @Mock private UriBuilder uriBuilder;

  @Before
  public void setup() {
    eventDao = spy(eventDao);
  }

  @Test
  public void getWithResults() throws Exception {
    URI uri =
        new URI(
            "http://api.eventful.com/json/events/search?apiKey=blah123&location=London&include=categories");
    String responseBody =
        IOUtils.toString(new ClassPathResource("fullOutput.json").getInputStream(), "utf-8");
    given(eventDao.responseBody(eq(uri))).willReturn(responseBody);
    EventResponse response = eventDao.get(uri);
    assertNotNull(response);
    assertEquals(Long.valueOf(2035l), response.getSize());
    assertEquals(Long.valueOf(204l), response.getPages());
    assertEquals("E0-001-113863883-8", response.getEventData().getEvents().get(0).getId());
    assertEquals(
        " An introduction to foraging, showing the wide range of different plants that are available in one of the more urban of London parks. Spring is a great time to go foraging with plenty of fresh green growth. We will cover at least twenty different edible plants and sample some foraged preparations along the way.  ",
        response.getEventData().getEvents().get(0).getDescription());
    assertEquals("London", response.getEventData().getEvents().get(0).getLocation());
    assertEquals("Foraging Course", response.getEventData().getEvents().get(0).getName());
    assertEquals("Finsbury Park", response.getEventData().getEvents().get(0).getVenue());
    assertEquals(
        "Other &amp; Miscellaneous",
        response
            .getEventData()
            .getEvents()
            .get(0)
            .getCategories()
            .getCategoryList()
            .get(0)
            .getName());
    DateTime startTime = response.getEventData().getEvents().get(0).getStartTime();
    assertEquals(2018, startTime.getYear());
    assertEquals(5, startTime.getMonthOfYear());
    assertEquals(5, startTime.getDayOfMonth());
    assertEquals(11, startTime.getHourOfDay());
    assertEquals(0, startTime.getMinuteOfHour());
    assertEquals(0, startTime.getSecondOfMinute());
  }

  @Test
  public void getWithCountOnly() throws Exception {
    URI uri =
        new URI(
            "http://api.eventful.com/json/events/search?apiKey=blah123&location=London&include=categories&count_only=true");
    String responseBody =
        IOUtils.toString(new ClassPathResource("countOnly.json").getInputStream(), "utf-8");
    given(eventDao.responseBody(eq(uri))).willReturn(responseBody);
    EventResponse response = eventDao.get(uri);
    assertNotNull(response);
    assertEquals(Long.valueOf(2007l), response.getSize());
    assertNull(response.getEventData());
    assertNull(response.getPages());
  }
}
