package com.stergion.githubbackend.contirbutions.entities;

import com.stergion.githubbackend.utilityTypes.CommitComment;
import com.stergion.githubbackend.utilityTypes.File;
import com.stergion.githubbackend.utilityTypes.Github;

import java.time.LocalDate;
import java.util.List;

public non-sealed class Commit extends Contribution {

    public LocalDate committedDate;
    public LocalDate pushedDate;
    public int additions;
    public int deletions;
    public List<CommitComment> comments;
    public int commentsCount;
    public List<Github> associatedPullRequest;
    public int associatedPullRequestsCount;
    public List<File> files;
    public int filesCount;

}
