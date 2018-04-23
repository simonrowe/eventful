package com.simonjamesrowe.events.converter;

import com.simonjamesrowe.events.es.model.Event;
import com.simonjamesrowe.events.model.SourceEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.stream.Collectors;

@Component
public class SourceEventConverter implements Converter<SourceEvent, Event> {

  @Override
  public Event convert(SourceEvent sourceEvent) {
    if (sourceEvent == null) {
      return null;
    }
    Event e = new Event();
    BeanUtils.copyProperties(sourceEvent, e, "startTime");
    if (sourceEvent.getCategories() != null
        && !CollectionUtils.isEmpty(sourceEvent.getCategories().getCategoryList())) {
      e.setCategories(
          sourceEvent
              .getCategories()
              .getCategoryList()
              .stream()
              .map(c -> c.getName())
              .collect(Collectors.toList()));
      e.setCategoryIds(
          sourceEvent
              .getCategories()
              .getCategoryList()
              .stream()
              .map(c -> c.getId())
              .collect(Collectors.toList()));
    }
    if (sourceEvent.getStartTime() != null) {
      Calendar cal = new GregorianCalendar();
      cal.setTimeInMillis(sourceEvent.getStartTime().getMillis());
      e.setStartTime(cal.getTime());
    }

    return e;
  }
}
