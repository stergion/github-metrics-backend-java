package com.stergion.githubbackend.infrastructure.persistence.utilityTypes;

public class CommitComment {
    public String author;
    public String publishedAt;
    public int position;
    public String reactionsCount;
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
