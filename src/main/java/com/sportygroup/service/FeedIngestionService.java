package com.sportygroup.service;

import com.sportygroup.entity.Event;

public interface FeedIngestionService {

    void processFeedAsync(Event event);
}
