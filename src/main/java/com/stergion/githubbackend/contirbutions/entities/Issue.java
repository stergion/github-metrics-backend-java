package com.stergion.githubbackend.contirbutions.entities;

import com.stergion.githubbackend.utilityTypes.IssueState;
import com.stergion.githubbackend.utilityTypes.Label;
import io.quarkus.mongodb.panache.common.MongoEntity;

import java.time.LocalDate;
import java.util.List;

@MongoEntity(collection = "issues")
public non-sealed class Issue extends Contribution {
    public LocalDate createdAt;
    public LocalDate closedAt;
    public LocalDate updatedAt;
    public IssueState state;
    public String title;
    public String body;
    public int reactionsCount;
    public List<Label> labels;
    public String closer;
}
