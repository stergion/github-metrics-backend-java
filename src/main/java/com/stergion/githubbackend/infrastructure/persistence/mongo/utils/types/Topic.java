package com.stergion.githubbackend.infrastructure.persistence.mongo.utils.types;

public class Topic {
    public String name;

    @Override
    public String toString() {
        return "{" +
                "name: '" + name + '\'' +
                "}";
    }
}
