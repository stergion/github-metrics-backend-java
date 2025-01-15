package com.stergion.githubbackend.infrastructure.persistence.codec;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import java.net.URI;

public class URICodecProvider implements CodecProvider {
    @Override
    public <T> Codec<T> get(Class<T> aClass, CodecRegistry codecRegistry) {
        if (URI.class.isAssignableFrom(aClass)) {
            return (Codec<T>) new URICodec();
        }
        return null;
    }
}
