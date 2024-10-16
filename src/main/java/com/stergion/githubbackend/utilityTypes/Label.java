package com.stergion.githubbackend.utilityTypes;

public class Label {
    public String name;
    public String description;

    @Override
    public String toString() {
        return "{ name: '" + name + '\'' +
                ", description: '" + description + '\'' +
                '}';
    }
}
