package com.stergion.githubbackend.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MongoIndexInitializer {
    @Inject
    MongoClient mongoClient;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String databaseName;

    @Startup
    void createIndexes() {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        createUserIndexes(database);
        createRepositoryIndexes(database);
        createContributionIndexes(database, "commits");
        createContributionIndexes(database, "issues");
        createContributionIndexes(database, "issueComments");
        createContributionIndexes(database, "pullRequests");
        createContributionIndexes(database, "pullRequestReviews");
    }

    private void createUserIndexes(MongoDatabase database) {
        MongoCollection<Document> collection = database.getCollection("users");

//        createIndexIfNotExists(collection, "_id", true);
        createIndexIfNotExists(collection, "login", true);
    }

    private void createRepositoryIndexes(MongoDatabase database) {
        MongoCollection<Document> collection = database.getCollection("repositories");

//        createIndexIfNotExists(collection, "_id", true);
        createIndexIfNotExists(collection,
                Indexes.compoundIndex(
                        Indexes.ascending("owner"),
                        Indexes.ascending("name")
                                     ),
                "nameWithOwner_index",
                true);
    }

    private void createContributionIndexes(MongoDatabase database, String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);

//        createIndexIfNotExists(collection, "_id", true);
        createIndexIfNotExists(collection, "github.id", true);
    }

    private void createIndexIfNotExists(MongoCollection<Document> collection, String field,
                                        boolean unique) {
        createIndexIfNotExists(collection, Indexes.ascending(field), field + "_1", unique);
    }

    private void createIndexIfNotExists(MongoCollection<Document> collection, Bson index,
                                        String indexName, boolean unique) {
        List<Document> existingIndexes = new ArrayList<>();
        collection.listIndexes().into(existingIndexes);

        boolean indexExists = existingIndexes.stream()
                                             .anyMatch(existing -> existing.get("name")
                                                                           .equals(indexName));

        if (!indexExists) {
            collection.createIndex(index, new IndexOptions().unique(unique).name(indexName));
        }
    }
}
