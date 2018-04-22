package com.simonjamesrowe.events.dao;

import com.simonjamesrowe.events.model.EventResponse;

import java.io.IOException;
import java.net.URI;

public interface EventDao {

  EventResponse get(URI uri) throws IOException;
}
