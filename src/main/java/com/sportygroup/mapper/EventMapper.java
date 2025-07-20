package com.sportygroup.mapper;

import com.bazaarvoice.jolt.Chainr;
import com.sportygroup.entity.BetSettlementEvent;
import com.sportygroup.entity.Event;
import com.sportygroup.entity.OddChangeEvent;
import com.sportygroup.entity.enums.BetOutcome;
import com.sportygroup.entity.enums.FeedProvider;
import com.sportygroup.entity.enums.EventType;
import com.sportygroup.exception.UnknownTypeException;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.sportygroup.mapper.EventMapperConstants.EVENT_TYPE_TRANSFORMER;
import static com.sportygroup.mapper.EventMapperConstants.EVENT_TYPE;
import static com.sportygroup.mapper.EventMapperConstants.MAPPER_PATH;
import static com.sportygroup.mapper.EventMapperConstants.OBJECT_MAPPER;
import static com.sportygroup.mapper.EventMapperConstants.TRANSFORMER_SUFFIX;
import static com.sportygroup.mapper.EventMapperConstants.UNKNOWN_OUTCOME_ERROR;


public class EventMapper {


    public static Event convertToEvent(FeedProvider feedProvider, Map<String, Object> feed) throws IOException {
        String providerMapperPath = String.format(MAPPER_PATH, feedProvider.name().toLowerCase());

        Map<String, Object> feedProviderMapper = OBJECT_MAPPER.readValue(new ClassPathResource(providerMapperPath).getContentAsByteArray(), Map.class);

        EventType eventType = getFeedTypeFromFeed(feedProviderMapper, feed);

        return getEventFromFeed(feedProvider, feedProviderMapper, eventType, feed);
    }

    private static EventType getFeedTypeFromFeed(Map<String, Object> feedProviderMapper, Map<String, Object> feed) {
        Object joltSpec = feedProviderMapper.get(EVENT_TYPE_TRANSFORMER);
        Chainr chainr = Chainr.fromSpec(joltSpec);

        Map<String, Object> transformed = (Map<String, Object>) chainr.transform(feed);
        String eventTypeStr = (String) transformed.get(EVENT_TYPE);

        return EventType.valueOf(eventTypeStr);
    }

    private static Event getEventFromFeed(FeedProvider feedProvider, Map<String, Object> feedProviderMapper, EventType eventType, Map<String, Object> feed) {
        Object joltSpec = feedProviderMapper.get(eventType.name() + TRANSFORMER_SUFFIX);
        Chainr chainr = Chainr.fromSpec(joltSpec);

        Map<String, Object> result = (Map<String, Object>) chainr.transform(feed);

        String eventId = (String) result.get("eventId");

        return switch (eventType) {
            case ODDS_CHANGE -> {
                OddChangeEvent event = new OddChangeEvent();
                event.setEventType(eventType);
                event.setEventId(eventId);
                Map<String, Object> rawOdds = (Map<String, Object>) result.get("odds");
                Map<BetOutcome, Double> odds = new HashMap<>();
                for (Map.Entry<String, Object> entry : rawOdds.entrySet()) {
                    BetOutcome outcome = BetOutcome.fromString(entry.getKey());
                    if (Objects.isNull(outcome)) {
                        throw new UnknownTypeException(String.format(UNKNOWN_OUTCOME_ERROR, entry.getKey()));
                    }
                    odds.put(outcome, ((Number) entry.getValue()).doubleValue());
                }
                event.setSource(feedProvider);
                event.setOdds(odds);
                yield event;
            }

            case BET_SETTLEMENT -> {
                BetSettlementEvent event = new BetSettlementEvent();
                event.setEventType(eventType);
                event.setEventId(eventId);
                BetOutcome outcome = BetOutcome.fromString((String) result.get("outcome"));
                if (Objects.isNull(outcome)) {
                    throw new UnknownTypeException(String.format(UNKNOWN_OUTCOME_ERROR, result.get("outcome")));
                }
                event.setOutcome(outcome);
                event.setSource(feedProvider);
                yield event;
            }
        };
    }


}
