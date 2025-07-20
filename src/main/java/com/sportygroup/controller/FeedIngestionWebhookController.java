package com.sportygroup.controller;

import com.sportygroup.entity.Event;
import com.sportygroup.entity.enums.FeedProvider;
import com.sportygroup.entity.IngestionResponse;
import com.sportygroup.exception.UnknownTypeException;
import com.sportygroup.mapper.EventMapper;
import com.sportygroup.service.FeedIngestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class FeedIngestionWebhookController {

    private final FeedIngestionService feedIngestionService;

    public FeedIngestionWebhookController(FeedIngestionService feedIngestionService) {
        this.feedIngestionService = feedIngestionService;
    }

    @PostMapping(value = "/{provider}/feed", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IngestionResponse> acceptFeed(@PathVariable(value = "provider") FeedProvider feedProvider,
                                                        @RequestBody Map<String, Object> feed) {

        if (Objects.isNull(feedProvider)) {
            return ResponseEntity.badRequest().body(IngestionResponse.builder()
                    .message("Feed provider with this path not registered")
                    .build());
        }

        Event event;
        try {
            event = EventMapper.convertToEvent(feedProvider, feed);
        } catch (UnknownTypeException uex) {
            log.error("Unknown type found while converting the event {} for provider {}", feed, feedProvider.name());
            return ResponseEntity.badRequest().body(IngestionResponse.builder()
                    .message(uex.getMessage()).build());
        } catch (Exception ex) {
            log.error("Error while converting the event {} for provider {}", feed, feedProvider.name());
            return ResponseEntity.internalServerError().body(IngestionResponse.builder()
                    .message("Something went wrong while parsing the event").build());
        }

        feedIngestionService.processFeedAsync(event);
        return ResponseEntity.ok(IngestionResponse.builder()
                .message("Accepted").build());
    }

}
