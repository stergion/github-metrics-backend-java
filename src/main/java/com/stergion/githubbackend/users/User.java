package com.stergion.githubbackend.users;
import com.stergion.githubbackend.utilityTypes.Github;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;


@MongoEntity(collection = "users")
public class User {
    public ObjectId id;
    public String login;
    public String name;
    public Github github;
    public LocalDate updatedAt;
    public List<ObjectId> repositories;
    public String avatarURL;
    public String bio;
    public String email;
    public String twitterHandle;
    public String websiteURL;
}
