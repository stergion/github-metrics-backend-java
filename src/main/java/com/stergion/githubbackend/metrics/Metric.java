package com.stergion.githubbackend.metrics;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@MongoEntity(collection = "metrics")
public class Metric {
    public ObjectId id;
    @BsonProperty("user_id")
    public ObjectId userId;
    @BsonProperty("repository_id")
    public ObjectId repositoryId;
}
