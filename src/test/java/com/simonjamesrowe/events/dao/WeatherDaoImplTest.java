package com.simonjamesrowe.events.dao;

import com.simonjamesrowe.events.model.WeatherResponse;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import java.math.BigDecimal;
import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class WeatherDaoImplTest {

  private static final String TEST_API_KEY = "12346578";

  @InjectMocks private WeatherDaoImpl weatherDao;

  @Before
  public void setup() {
    weatherDao = spy(weatherDao);
  }

  @Test
  public void getWithResults() throws Exception {
    URI uri =
        new URI(
            "http://api.openweathermap.org/data/2.5/forecast?q=London,gb&mode=json&APPID=test123456&units=metric");
    String responseBody =
        IOUtils.toString(new ClassPathResource("weather.json").getInputStream(), "utf-8");
    given(weatherDao.responseBody(eq(uri))).willReturn(responseBody);
    WeatherResponse response = weatherDao.get(uri);
    assertNotNull(response);
    assertEquals(new BigDecimal("16.14"), response.getItems().get(0).getMainItem().getMinTemp());
    assertEquals(new BigDecimal("17.24"), response.getItems().get(0).getMainItem().getMaxTemp());
    assertEquals(Integer.valueOf(55), response.getItems().get(0).getMainItem().getHumidity());
    assertEquals("light rain", response.getItems().get(0).getDescription().get(0).getDescription());
    assertEquals("10d", response.getItems().get(0).getDescription().get(0).getIcon());
    assertEquals(2018, response.getItems().get(0).getStartTime().getYear());
    assertEquals(4, response.getItems().get(0).getStartTime().getMonthOfYear());
    assertEquals(22, response.getItems().get(0).getStartTime().getDayOfMonth());
    assertEquals(18, response.getItems().get(0).getStartTime().getHourOfDay());
  }
}
