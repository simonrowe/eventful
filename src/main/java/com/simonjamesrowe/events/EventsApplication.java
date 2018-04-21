package com.simonjamesrowe.events;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

@SpringBootApplication
public class EventsApplication {

  public static void main(String[] args) {
    SpringApplication.run(EventsApplication.class, args);
  }

  @Bean
  public UriBuilder uriBuilder(@Value("${events.rest.endpoint}") String eventsRestEndpoint) {
    return new DefaultUriBuilderFactory(eventsRestEndpoint).builder();
  }
}
