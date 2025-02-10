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
        assertEquals(TEST_LOGIN, issue.user());
        assertEquals("test-repo", issue.repository().name());
        assertEquals("owner", issue.repository().owner());
        assertEquals("123", issue.github().id());
        assertEquals(URI.create("https://example.com"), issue.github().url());
        assertEquals(date, issue.createdAt());
        assertEquals(date, issue.updatedAt());
        assertEquals(date, issue.closedAt());
        assertEquals(IssueState.CLOSED, issue.state());
        assertEquals("Test Issue", issue.title());
        assertEquals("Test body", issue.body());
        assertEquals(5, issue.reactionsCount());
        assertEquals("closer", issue.closer());

        // Verify labels
        assertEquals(2, issue.labels().size());
        assertTrue(issue.labels().stream().anyMatch(l -> l.name().equals("bug")));
        assertTrue(issue.labels().stream().anyMatch(l -> l.name().equals("feature")));
        assertEquals("Bug description", issue.labels().stream()
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
        assertNotNull(issue.labels());
        assertTrue(issue.labels().isEmpty());
        assertNull(issue.closer());
        assertEquals(0, issue.reactionsCount());
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