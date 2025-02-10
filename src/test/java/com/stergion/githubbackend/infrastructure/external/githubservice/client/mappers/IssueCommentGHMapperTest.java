package com.stergion.githubbackend.infrastructure.external.githubservice.client.mappers;

import com.stergion.githubbackend.domain.contirbutions.models.IssueComment;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.IssueCommentGH;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.GitHubNodeRef;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.Reactions;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.RepositoryOwner;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.RepositoryRef;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class IssueCommentGHMapperTest {

    @Inject
    IssueCommentGHMapper mapper;

    private static final String TEST_LOGIN = "testUser";

    @Test
    @DisplayName("Should map all fields correctly with pull request reference")
    void shouldMapAllFieldsWithPullRequest() {
        // Given
        LocalDate date = LocalDate.now();
        var commentGH = createFullCommentWithPullRequest(date);

        // When
        IssueComment ic = mapper.toDomain(commentGH, TEST_LOGIN);

        // Then
        assertNotNull(ic);
        assertEquals(TEST_LOGIN, ic.user());
        assertEquals("test-repo", ic.repository().name());
        assertEquals("owner", ic.repository().owner());
        assertEquals("123", ic.github().id());
        assertEquals(URI.create("https://example.com"), ic.github().url());
        assertEquals(date, ic.createdAt());
        assertEquals(date.plusDays(1), ic.updatedAt());
        assertEquals(date.plusDays(2), ic.publishedAt());
        assertEquals(date.plusDays(3), ic.lastEditedAt());
        assertEquals("Test body", ic.body());

        // Verify associated pull request
        assertNotNull(ic.associatedIssue());
        assertEquals(IssueComment.IssueType.PULL_REQUEST, ic.associatedIssue().type());
        assertEquals("pr-123", ic.associatedIssue().github().id());
    }

    @Test
    @DisplayName("Should map all fields correctly with issue reference")
    void shouldMapAllFieldsWithIssue() {
        // Given
        LocalDate date = LocalDate.now();
        var commentGH = createFullCommentWithIssue(date);

        // When
        IssueComment ic = mapper.toDomain(commentGH, TEST_LOGIN);

        // Then
        assertNotNull(ic);
        assertEquals(TEST_LOGIN, ic.user());
        assertEquals("test-repo", ic.repository().name());
        assertEquals("owner", ic.repository().owner());
        assertEquals("123", ic.github().id());
        assertEquals(URI.create("https://example.com"), ic.github().url());

        // Verify associated issue
        assertNotNull(ic.associatedIssue());
        assertEquals(IssueComment.IssueType.ISSUE, ic.associatedIssue().type());
        assertEquals("issue-123", ic.associatedIssue().github().id());
    }

    @Test
    @DisplayName("Should handle minimal fields correctly")
    void shouldHandleMinimalFields() {
        // Given
        LocalDate date = LocalDate.now();
        var commentGH = createMinimalComment(date);

        // When
        IssueComment ic = mapper.toDomain(commentGH, TEST_LOGIN);

        // Then
        assertNotNull(ic);
        assertEquals(TEST_LOGIN, ic.user());
        assertEquals(date, ic.createdAt());
        assertEquals(date, ic.updatedAt());
        assertNull(ic.publishedAt());
        assertNull(ic.lastEditedAt());
        assertNull(ic.associatedIssue());
    }

    private IssueCommentGH createFullCommentWithPullRequest(LocalDate date) {
        return new IssueCommentGH(
                "123",
                URI.create("https://example.com"),
                date,
                date.plusDays(1),
                date.plusDays(2),
                date.plusDays(3),
                "Test body",
                new RepositoryRef("test-repo", new RepositoryOwner("owner")),
                null,
                new GitHubNodeRef("pr-123", URI.create("https://example.com/pr")),
                new Reactions(5)
        );
    }

    private IssueCommentGH createFullCommentWithIssue(LocalDate date) {
        return new IssueCommentGH(
                "123",
                URI.create("https://example.com"),
                date,
                date.plusDays(1),
                date.plusDays(2),
                date.plusDays(3),
                "Test body",
                new RepositoryRef("test-repo", new RepositoryOwner("owner")),
                new GitHubNodeRef("issue-123", URI.create("https://example.com/issue")),
                null,
                new Reactions(5)
        );
    }

    private IssueCommentGH createMinimalComment(LocalDate date) {
        return new IssueCommentGH(
                "123",
                URI.create("https://example.com"),
                date,
                date,
                null,
                null,
                "Test body",
                new RepositoryRef("test-repo", new RepositoryOwner("owner")),
                null,
                null,
                new Reactions(0)
        );
    }
}