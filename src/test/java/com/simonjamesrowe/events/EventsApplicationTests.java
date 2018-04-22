package com.simonjamesrowe.events;

import com.simonjamesrowe.events.service.EventServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = EventsApplication.class
)
public class EventsApplicationTests {

  @Autowired private TestRestTemplate testRestTemplate;

  @Autowired EventServiceImpl eventService;

  @Before
  public void before() throws IOException {
    eventService.loadIntoElasticSearch(4);
  }

  @Test
  public void contextLoads() {}
}
