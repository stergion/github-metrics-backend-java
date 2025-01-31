package com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes;

public class Topic {
    public String name;

    @Override
    public String toString() {
        return "{" +
                "name: '" + name + '\'' +
                "}";
    }
}
