package com.sportygroup.entity.enums;

import com.fasterxml.jackson.annotation.JsonGetter;

public enum FeedProvider {

    ALPHA("provider-alpha"), BETA("provider-beta");

    private final String requestPath;

    FeedProvider(String requestPath) {
        this.requestPath = requestPath;
    }

    @JsonGetter
    public static FeedProvider getFeedProvider(String requestPath) {
        for (FeedProvider provider : FeedProvider.values()) {
            if (provider.requestPath.equals(requestPath)) {
                return provider;
            }
        }
        return null;
    }
}
