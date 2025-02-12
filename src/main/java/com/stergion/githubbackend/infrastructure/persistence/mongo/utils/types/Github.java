package com.stergion.githubbackend.infrastructure.persistence.mongo.utils.types;

import org.bson.codecs.pojo.annotations.BsonProperty;

import java.net.URI;

public class Github {
    @BsonProperty("id")
    public String id;
    @BsonProperty("url")
    public URI url;

    @Override
    public String toString() {
        return "{ id: '" + id + '\'' +
                ", url: '" + url + '\'' +
                '}';
    }
}
