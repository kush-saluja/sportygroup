package com.sportygroup.service;

import com.sportygroup.entity.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FeedIngestionServiceImpl implements FeedIngestionService {

    @Override
    public void processFeedAsync(Event event) {
        log.info("Received eventId {} of type {} from source {}", event.getEventId(),
                event.getEventType(), event.getSource());

        log.info("Processing event {}", event);

        log.info("Successfully processed event with id {}", event.getEventId());
    }
}
