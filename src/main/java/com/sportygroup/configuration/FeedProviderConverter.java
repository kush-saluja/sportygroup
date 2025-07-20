package com.sportygroup.configuration;


import com.sportygroup.entity.enums.FeedProvider;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FeedProviderConverter implements Converter<String, FeedProvider> {
    @Override
    public FeedProvider convert(String source) {
        return FeedProvider.getFeedProvider(source);
    }
}

