package com.simonjamesrowe.events.es.dao;

import com.simonjamesrowe.events.es.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

public interface ElasticSearchEventDao extends ElasticsearchCrudRepository<Event, String> {

  @Query("{\"match\": {\"_all\": \"?0\"} }")
  Page<Event> findAll(String search, Pageable pageable);
}
