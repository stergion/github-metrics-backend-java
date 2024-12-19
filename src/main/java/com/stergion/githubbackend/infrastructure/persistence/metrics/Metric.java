package com.stergion.githubbackend.infrastructure.persistence.metrics;

import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotNull;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@MongoEntity(collection = "metrics")
public class Metric {
    @NotNull
    public ObjectId id;

    @NotNull
    @BsonProperty("user_id")
    public ObjectId userId;

    @NotNull
    @BsonProperty("repository_id")
    public ObjectId repositoryId;

    @Override
    public String toString() {
        return """
                {
                  id: %s,
                  userId: %s,
                  repositoryId: %s
                }
                """.formatted(id, userId, repositoryId);
    }
}
