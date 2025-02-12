package com.stergion.githubbackend.infrastructure.external.githubservice.client.mappers;

import com.stergion.githubbackend.domain.contirbutions.models.Issue;
import com.stergion.githubbackend.domain.utils.types.IssueState;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.IssueGH;
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
class IssueGHMapperTest {

    @Inject
    IssueGHMapper mapper;

    private static final String TEST_LOGIN = "testUser";

    @Test
    @DisplayName("Should map all fields correctly")
    void shouldMapAllFieldsCorrectly() {
        // Given
        LocalDate date = LocalDate.now();
        var issueGH = createFullIssue(date);

        // When
        Issue issue = mapper.toDomain(issueGH, TEST_LOGIN);

        // Then
        assertNotNull(issue);
        assertNull(issue.getUser().id());
        assertNull(issue.getRepository().id());
        assertEquals(TEST_LOGIN, issue.getUser().login());
        assertEquals("test-repo", issue.getRepository().name());
        assertEquals("owner", issue.getRepository().owner());
        assertEquals("123", issue.getGithub().id());
        assertEquals(URI.create("https://example.com"), issue.getGithub().url());
        assertEquals(date, issue.getCreatedAt());
        assertEquals(date, issue.getUpdatedAt());
        assertEquals(date, issue.getClosedAt());
        assertEquals(IssueState.CLOSED, issue.getState());
        assertEquals("Test Issue", issue.getTitle());
        assertEquals("Test body", issue.getBody());
        assertEquals(5, issue.getReactionsCount());
        assertEquals("closer", issue.getCloser());

        // Verify labels
        assertEquals(2, issue.getLabels().size());
        assertTrue(issue.getLabels().stream().anyMatch(l -> l.name().equals("bug")));
        assertTrue(issue.getLabels().stream().anyMatch(l -> l.name().equals("feature")));
        assertEquals("Bug description", issue.getLabels().stream()
                                           .filter(l -> l.name().equals("bug"))
                                           .findFirst()
                                           .get()
                                           .description());
    }

    @Test
    @DisplayName("Should handle null collections properly")
    void shouldHandleNullCollections() {
        // Given
        LocalDate date = LocalDate.now();
        var issueGH = createMinimalIssue(date);

        // When
        Issue issue = mapper.toDomain(issueGH, TEST_LOGIN);

        // Then
        assertNotNull(issue);
        assertNotNull(issue.getLabels());
        assertTrue(issue.getLabels().isEmpty());
        assertNull(issue.getCloser());
        assertEquals(0, issue.getReactionsCount());
    }

    private IssueGH createFullIssue(LocalDate date) {
        return new IssueGH(
                "123",
                URI.create("https://example.com"),
                date,
                date,
                date,
                IssueGH.State.CLOSED,
                "Test Issue",
                "Test body",
                new RepositoryRef("test-repo", new RepositoryOwner("owner")),
                createTimelineItems(),
                new Reactions(5),
                createLabelsConnection(),
                new IssueGH.CommentsConnection(2)
        );
    }

    private IssueGH createMinimalIssue(LocalDate date) {
        return new IssueGH(
                "123",
                URI.create("https://example.com"),
                date,
                date,
                null,
                IssueGH.State.OPEN,
                "Test Issue",
                "Test body",
                new RepositoryRef("test-repo", new RepositoryOwner("owner")),
                null,
                new Reactions(0),
                null,
                new IssueGH.CommentsConnection(0)
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

    private IssueGH.TimelineItems createTimelineItems() {
        return new IssueGH.TimelineItems(
                List.of(new IssueGH.TimelineItem(new Author("closer")))
        );
    }
}