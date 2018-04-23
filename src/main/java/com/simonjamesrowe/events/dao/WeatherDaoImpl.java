package com.simonjamesrowe.events.dao;

import com.simonjamesrowe.events.model.WeatherResponse;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;

@Repository
public class WeatherDaoImpl extends AbstractHttpDao<WeatherResponse> implements WeatherDao {

  @Override
  public WeatherResponse get(URI uri) throws IOException {
    return super.get(uri, WeatherResponse.class);
  }
}
