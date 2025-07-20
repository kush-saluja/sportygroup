package com.sportygroup.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EventMapperConstants {

    protected static final String MAPPER_PATH = "/templates/%s_mapper.json";
    protected static final String EVENT_TYPE_TRANSFORMER = "EVENT_TYPE_TRANSFORMER";
    protected static final String EVENT_TYPE = "eventType";
    protected static final String TRANSFORMER_SUFFIX = "_TRANSFORMER";
    protected static final String UNKNOWN_OUTCOME_ERROR = "Cannot convert bet outcome type %s to known outcomes";

    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
}
