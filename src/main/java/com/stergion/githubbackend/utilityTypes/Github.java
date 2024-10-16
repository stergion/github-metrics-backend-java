package com.stergion.githubbackend.utilityTypes;

public class Github {
    public String id;
    public String url;

    @Override
    public String toString() {
        return "{ id: '" + id + '\'' +
                ", url: '" + url + '\'' +
                '}';
    }
}
