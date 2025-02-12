package com.stergion.githubbackend.infrastructure.persistence.mongo.utils.types;

import java.time.LocalDateTime;

public class CommitComment {
    public String author;
    public LocalDateTime publishedAt;
    public int position;
    public int reactionsCount;
    public String body;

    @Override
    public String toString() {
        return "{ author: '" + author + '\'' +
                ", publishedAt: '" + publishedAt + '\'' +
                ", position: " + position +
                ", reactionsCount: '" + reactionsCount + '\'' +
                ", body: '" + body + '\'' +
                '}';
    }
}
