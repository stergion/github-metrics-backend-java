package com.stergion.githubbackend.infrastructure.persistence.utilityTypes;

public class Topic {
    public String name;

    @Override
    public String toString() {
        return "{" +
                "name: '" + name + '\'' +
                "}";
    }
}
