package com.sportygroup.entity;

import com.sportygroup.entity.enums.FeedProvider;
import com.sportygroup.entity.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Event {

    private EventType eventType;
    private String eventId;
    private FeedProvider source;
}
