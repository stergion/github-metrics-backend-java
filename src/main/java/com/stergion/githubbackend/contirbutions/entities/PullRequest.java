package com.stergion.githubbackend.contirbutions.entities;

import com.stergion.githubbackend.utilityTypes.Github;
import com.stergion.githubbackend.utilityTypes.IssueState;
import com.stergion.githubbackend.utilityTypes.Label;

import java.time.LocalDate;
import java.util.List;

public non-sealed class PullRequest extends Contribution {
    public LocalDate createdAt;
    public LocalDate mergedAt;
    public LocalDate closedAt;
    public LocalDate updatedAt;
    public IssueState state;
    public int reactionsCount;
    public List<Label> labels;
    public String title;
    public String body;
    public List<Commit> commits;
    public int commitsCount;
    public int commentsCount;
    public List<Github> closingIssuesReferences;
    public int closingIssuesReferencesCount;
}
