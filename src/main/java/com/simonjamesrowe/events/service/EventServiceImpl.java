package com.simonjamesrowe.events.service;

import com.simonjamesrowe.events.converter.SourceEventConverter;
import com.simonjamesrowe.events.dao.EventDao;
import com.simonjamesrowe.events.dao.WeatherDao;
import com.simonjamesrowe.events.es.dao.ElasticSearchEventDao;
import com.simonjamesrowe.events.es.model.Event;
import com.simonjamesrowe.events.model.EventResponse;
import com.simonjamesrowe.events.model.WeatherResponse;
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

  @Value("${weather.api.endpoint}")
  private String weatherRestEndpoint;

  @Value("${weather.api.key}")
  private String weatherApiKey;

  @Value("${weather.api.image}")
  private String weatherIconUrl;

  @Value("${events.api.param.location}")
  private String location;

  @Value("${events.api.param.include}")
  private String include;

  @Autowired private WeatherDao weatherDao;

  @Autowired private ElasticsearchTemplate elasticsearchTemplate;

  private UriBuilder eventUriBuilder() {
    DefaultUriBuilderFactory factory =
        new DefaultUriBuilderFactory(
            eventsRestEndpoint
                + "?app_key={appKey}&location={location}&include={include}&sort_order={sort_order}");
    return factory.builder();
  }

  @Autowired private EventDao sourceEventDao;

  @Autowired private ElasticSearchEventDao elasticSearchEventDao;

  @Autowired private SourceEventConverter sourceEventConverter;

  /** Runs every half hour */
  @Scheduled(fixedRate = 60 * 30 * 1000)
  public void loadIntoElasticSearch() throws IOException {
    WeatherResponse weather = weather();
    long pages = calculatePages();
    for (int i = 1; i <= pages; i++) {
      loadPageIntoElasticSearch(i, weather);
    }
  }

  public void loadIntoElasticSearch(int maxPages) throws IOException {
    WeatherResponse weather = weather();
    long pages = calculatePages();
    for (int i = 1; i <= pages && i <= maxPages; i++) {
      loadPageIntoElasticSearch(i, weather);
    }
  }

  protected WeatherResponse weather() throws IOException {
    return weatherDao.get(
        new DefaultUriBuilderFactory(weatherRestEndpoint).builder().build(weatherApiKey));
  }

  private void loadPageIntoElasticSearch(int pageNumber, WeatherResponse weatherResponse)
      throws IOException {
    EventResponse eventResponse =
        sourceEventDao.get(
            eventUriBuilder()
                .query("page_size={pageSize}")
                .query("page_number={pageNumber}")
                .build(
                    apiKey,
                    location,
                    include,
                    "date",
                    String.valueOf(pageSize),
                    String.valueOf(pageNumber)));
    WeatherDecorator weatherDecorator = new WeatherDecorator(weatherResponse, weatherIconUrl);
    List<Event> elasticSearchDocuments =
        eventResponse
            .getEventData()
            .getEvents()
            .stream()
            .map(e -> sourceEventConverter.convert(e))
            .map(w -> weatherDecorator.decorate(w))
            .collect(Collectors.toList());
    elasticSearchEventDao.saveAll(elasticSearchDocuments);
  }

  private long calculatePages() throws IOException {
    EventResponse eventResponse =
        sourceEventDao.get(
            eventUriBuilder()
                .query("count_only={count_only}")
                .build(apiKey, location, include, "date", "true"));
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
