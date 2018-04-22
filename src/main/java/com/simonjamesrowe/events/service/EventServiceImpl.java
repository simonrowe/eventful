package com.simonjamesrowe.events.service;

import com.simonjamesrowe.events.converter.SourceEventConverter;
import com.simonjamesrowe.events.dao.EventDao;
import com.simonjamesrowe.events.es.dao.ElasticSearchEventDao;
import com.simonjamesrowe.events.es.model.Event;
import com.simonjamesrowe.events.model.EventResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements InitializingBean {

  @Value("${load.page.size}")
  private int pageSize;

  @Value("${events.api.key}")
  private String apiKey;

  @Value("${events.rest.endpoint}")
  private String eventsRestEndpoint;

  @Value("${events.api.param.location}")
  private String location;

  @Value("${events.api.param.include}")
  private String include;

  @Autowired private ElasticsearchTemplate elasticsearchTemplate;

  private UriBuilder uriBuilder() {
    DefaultUriBuilderFactory factory =
        new DefaultUriBuilderFactory(
            eventsRestEndpoint + "?app_key={appKey}&location={location}&include={include}");
    return factory.builder();
  }

  @Autowired private EventDao sourceEventDao;

  @Autowired private ElasticSearchEventDao elasticSearchEventDao;

  @Autowired private SourceEventConverter sourceEventConverter;

  /** Runs every half hour */
  @Scheduled(fixedRate = 60 * 30 * 1000)
  public void loadIntoElasticSearch() throws IOException {

    long pages = calculatePages();
    for (int i = 1; i <= pages; i++) {
      loadPageIntoElasticSearch(i);
    }
  }

  private void loadPageIntoElasticSearch(int pageNumber) throws IOException {
    EventResponse eventResponse =
        sourceEventDao.get(
            uriBuilder()
                .query("page_size={pageSize}")
                .query("page_number={pageNumber}")
                .build(
                    apiKey,
                    location,
                    include,
                    String.valueOf(pageSize),
                    String.valueOf(pageNumber)));
    List<Event> elasticSearchDocuments =
        eventResponse
            .getEventData()
            .getEvents()
            .stream()
            .map(e -> sourceEventConverter.convert(e))
            .collect(Collectors.toList());
    elasticSearchEventDao.saveAll(elasticSearchDocuments);
  }

  private long calculatePages() throws IOException {
    EventResponse eventResponse =
        sourceEventDao.get(
            uriBuilder().query("count_only={count_only}").build(apiKey, location, include, "true"));
    long size = eventResponse.getSize();
    return size % pageSize == 0 ? size / pageSize : (size / pageSize) + 1;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    elasticsearchTemplate.deleteIndex(Event.class);
    elasticsearchTemplate.createIndex(Event.class);
    elasticsearchTemplate.putMapping(Event.class);
    elasticsearchTemplate.refresh(Event.class);
  }
}
