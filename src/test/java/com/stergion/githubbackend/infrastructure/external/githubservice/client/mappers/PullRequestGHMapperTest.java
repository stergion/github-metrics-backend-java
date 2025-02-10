package com.stergion.githubbackend.infrastructure.external.githubservice.client.mappers;

import com.stergion.githubbackend.domain.contirbutions.models.PullRequest;
import com.stergion.githubbackend.domain.utils.types.Github;
import com.stergion.githubbackend.domain.utils.types.PullRequestCommit;
import com.stergion.githubbackend.domain.utils.types.PullRequestState;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.PullRequestGH;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.*;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class PullRequestGHMapperTest {

    @Inject
    PullRequestGHMapper mapper;

    private static final String TEST_LOGIN = "testUser";

    @Test
    @DisplayName("Should map all fields correctly")
    void shouldMapAllFieldsCorrectly() {
        // Given
        LocalDate date = LocalDate.now();
        var pullRequestGH = createFullPullRequest(date);

        // When
        PullRequest pr = mapper.toDomain(pullRequestGH, TEST_LOGIN);

        // Then
        assertNotNull(pr);
        assertEquals(TEST_LOGIN, pr.user());
        assertEquals("test-repo", pr.repository().name());
        assertEquals("owner", pr.repository().owner());
        assertEquals("123", pr.github().id());
        assertEquals(URI.create("https://example.com"), pr.github().url());
        assertEquals(date, pr.createdAt());
        assertEquals(date, pr.updatedAt());
        assertEquals(date, pr.mergedAt());
        assertEquals(date, pr.closedAt());
        assertEquals(PullRequestState.OPEN, pr.state());
        assertEquals("Test PR", pr.title());

        // Verify labels
        assertEquals(2, pr.labels().size());
        assertTrue(pr.labels().stream().anyMatch(l -> l.name().equals("bug")));
        assertTrue(pr.labels().stream().anyMatch(l -> l.name().equals("feature")));
        assertEquals("Bug description", pr.labels().stream()
                                           .filter(l -> l.name().equals("bug"))
                                           .findFirst()
                                           .get()
                                           .description());

        // Verify commits
        assertEquals(1, pr.commits().size());
        PullRequestCommit commit = pr.commits().getFirst();
        assertEquals("commit1", commit.github().id());
        assertEquals(10, commit.additions());
        assertEquals(5, commit.deletions());

        // Verify closing issues
        assertEquals(1, pr.closingIssuesReferences().size());
        Github issue = pr.closingIssuesReferences().getFirst();
        assertEquals("issue1", issue.id());
        assertEquals(URI.create("https://example.com/issue1"), issue.url());
    }

    @Test
    @DisplayName("Should handle null collections properly")
    void shouldHandleNullCollections() {
        // Given
        LocalDate date = LocalDate.now();
        var pullRequestGH = createMinimalPullRequest(date);

        // When
        PullRequest pr = mapper.toDomain(pullRequestGH, TEST_LOGIN);

        // Then
        assertNotNull(pr);
        assertNotNull(pr.labels());
        assertNotNull(pr.closingIssuesReferences());
        assertTrue(pr.labels().isEmpty());
        assertTrue(pr.closingIssuesReferences().isEmpty());
    }

    private PullRequestGH createFullPullRequest(LocalDate date) {
        return new PullRequestGH(
                "123",
                URI.create("https://example.com"),
                date,
                date,
                date,
                date,
                PullRequestGH.State.OPEN,
                "Test PR",
                "Test body",
                new RepositoryRef("test-repo", new RepositoryOwner("owner")),
                new Reactions(5),
                createLabelsConnection(),
                createCommitsConnection(),
                new PullRequestGH.CommentsConnection(0),
                createClosingIssuesConnection()
        );
    }

    private PullRequestGH createMinimalPullRequest(LocalDate date) {
        return new PullRequestGH(
                "123",
                URI.create("https://example.com"),
                date,
                date,
                null,
                null,
                PullRequestGH.State.OPEN,
                "Test PR",
                "Test body",
                new RepositoryRef("test-repo", new RepositoryOwner("owner")),
                new Reactions(0),
                null,
                createCommitsConnection(),
                new PullRequestGH.CommentsConnection(0),
                null
        );
    }

    private LabelsConnection createLabelsConnection() {
        return new LabelsConnection(
                2,
                List.of(
                        new LabelNode("bug", "Bug description"),
                        new LabelNode("feature", "Feature description")
                       )
        );
    }

    private PullRequestGH.CommitsConnection createCommitsConnection() {
        var commit = new PullRequestGH.CommitNode(
                new PullRequestGH.CommitNode.PullRequestCommit(
                        "commit1",
                        URI.create("https://example.com/commit1"),
                        2,
                        10,
                        5
                )
        );
        return new PullRequestGH.CommitsConnection(1, List.of(commit));
    }

    private PullRequestGH.ClosingIssuesReferences createClosingIssuesConnection() {
        var issueRef = new GitHubNodeRef("issue1", URI.create("https://example.com/issue1"));
        return new PullRequestGH.ClosingIssuesReferences(1, List.of(issueRef));
    }
}