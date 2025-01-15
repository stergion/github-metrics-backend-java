package com.stergion.githubbackend.infrastructure.persistence.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.net.URI;

public class URICodec implements Codec<URI> {
    @Override
    public URI decode(BsonReader bsonReader, DecoderContext decoderContext) {
        return URI.create(bsonReader.readString());
    }

    @Override
    public void encode(BsonWriter bsonWriter, URI uri, EncoderContext encoderContext) {
        bsonWriter.writeString(uri.toString());
    }

    @Override
    public Class<URI> getEncoderClass() {
        return URI.class;
    }
}
