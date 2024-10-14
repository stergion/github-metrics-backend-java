package com.stergion.githubbackend.metrics;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Metric {
    public ObjectId id;
    @BsonProperty("user_id")
    public ObjectId userId;
    @BsonProperty("repository_id")
    public ObjectId repositoryId;
}
