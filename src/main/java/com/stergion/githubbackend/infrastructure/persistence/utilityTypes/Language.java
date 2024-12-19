package com.stergion.githubbackend.infrastructure.persistence.utilityTypes;

public class Language {
    public String name;
    public int size;
    public float percentage;

    @Override
    public String toString() {
        return "{" +
                "name: '" + name + '\'' +
                ", size: " + size +
                ", percentage: " + percentage +
                "}";
    }
}
