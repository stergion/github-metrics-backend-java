package com.stergion.githubbackend.users;

import com.stergion.githubbackend.utilityTypes.Github;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;


@MongoEntity(collection = "users")
public class User {
    @NotNull
    public ObjectId id;

    @NotBlank
    public String login;

    @NotBlank
    public String name;

    @NotNull
    public Github github;

    @Email
    public String email;

    @PastOrPresent
    public LocalDate updatedAt;

    @NotNull
    public List<ObjectId> repositories;

    public String avatarURL;
    public String bio;
    public String twitterHandle;
    public String websiteURL;
}
