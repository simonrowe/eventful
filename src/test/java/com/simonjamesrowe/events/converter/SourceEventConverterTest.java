package com.simonjamesrowe.events.converter;

import com.simonjamesrowe.events.es.model.Event;
import com.simonjamesrowe.events.model.Categories;
import com.simonjamesrowe.events.model.Category;
import com.simonjamesrowe.events.model.SourceEvent;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class SourceEventConverterTest {

  private SourceEventConverter sourceEventConverter;

  @Before
  public void setup() {
    sourceEventConverter = new SourceEventConverter();
  }

  @Test
  public void testConvert() {
    SourceEvent event =
        sourceEvent(
            "id1234",
            "London",
            "Test Event",
            "A truely exciting test event",
            "22-Apr-2018 10:45:00",
            "London Bridge");
    addCategory(event, "Other", "oth");
    addCategory(event, "Exciting", "exc");
    Event e = sourceEventConverter.convert(event);
    assertNotNull(e);
    assertEquals("id1234", e.getId());
    assertEquals("London", e.getLocation());
    assertEquals("Test Event", e.getName());
    assertEquals("Test Event", e.getNameSort());
    assertEquals("A truely exciting test event", e.getDescription());
    assertEquals(e.getStartTime().getTime(), event.getStartTime().getMillis());
    assertEquals("London Bridge", e.getVenue());
    assertEquals(Arrays.asList("Other", "Exciting"), e.getCategories());
    assertEquals(Arrays.asList("oth", "exc"), e.getCategoryIds());
    assertEquals("Other", e.getCategorySort());
  }

  @Test
  public void testConvertNull() {
    assertNull(sourceEventConverter.convert(null));
  }

  private void addCategory(SourceEvent event, String category, String categoryId) {
    Category cat = new Category();
    cat.setId(categoryId);
    cat.setName(category);
    event.getCategories().getCategoryList().add(cat);
  }

  private SourceEvent sourceEvent(
      String id, String location, String name, String description, String startDate, String venue) {
    SourceEvent sourceEvent = new SourceEvent();
    sourceEvent.setId(id);
    sourceEvent.setLocation(location);
    sourceEvent.setName(name);
    sourceEvent.setDescription(description);
    sourceEvent.setVenue(venue);
    Categories category = new Categories();
    category.setCategoryList(new ArrayList<>());
    sourceEvent.setCategories(category);
    DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MMM-yyyy HH:mm:ss");
    DateTime dt = formatter.parseDateTime(startDate);
    sourceEvent.setStartTime(dt);
    return sourceEvent;
  }
}
