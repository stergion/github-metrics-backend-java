package com.stergion.githubbackend.infrastructure.persistence.utilityTypes;

import java.net.URI;

public class Github {
    public String id;
    public URI url;

    @Override
    public String toString() {
        return "{ id: '" + id + '\'' +
                ", url: '" + url + '\'' +
                '}';
    }
}
