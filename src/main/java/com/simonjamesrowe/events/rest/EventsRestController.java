package com.simonjamesrowe.events.rest;

import com.simonjamesrowe.events.es.dao.ElasticSearchEventDao;
import com.simonjamesrowe.events.es.model.Event;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventsRestController {

  @Autowired private ElasticSearchEventDao elasticSearchEventDao;

  @GetMapping("/api/events")
  public Page<Event> events(
      @RequestParam(defaultValue = "", required = false) String q, Pageable pageable) {
    if (StringUtils.isNotEmpty(q)) {
      return elasticSearchEventDao.findAll(q, pageable);
    } else {
      return elasticSearchEventDao.findAll(pageable);
    }
  }
}
