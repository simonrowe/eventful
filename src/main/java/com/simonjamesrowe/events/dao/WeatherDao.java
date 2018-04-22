package com.simonjamesrowe.events.dao;

import com.simonjamesrowe.events.model.WeatherResponse;

import java.io.IOException;
import java.net.URI;

public interface WeatherDao {
  WeatherResponse get(URI uri) throws IOException;
}
