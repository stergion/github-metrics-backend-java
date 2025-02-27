package com.stergion.githubbackend.infrastructure.external.githubservice.client.mappers;

import com.stergion.githubbackend.domain.contirbutions.models.Commit;
import com.stergion.githubbackend.domain.utils.types.CommitComment;
import com.stergion.githubbackend.domain.utils.types.File;
import com.stergion.githubbackend.domain.utils.types.Github;
import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.CommitGH;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.Author;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.CommitFile;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.Reactions;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CommitGHMapperTest {

    @Inject
    CommitGHMapper mapper;

    @Test
    @DisplayName("Should map a complete CommitGH to Commit")
    void shouldMapCompleteCommit() throws Exception {
        // Arrange
        String userLogin = "testUser";
        URI commitUrl = new URI("https://github.com/test/commit/123");
        LocalDate committedDate = LocalDate.now();
        String commitMessage = "Test commit";

        CommitGH.CommitComment comment = new CommitGH.CommitComment(
                LocalDate.now(),
                1,
                "Test comment",
                new Author("commenter"),
                new Reactions(5)
        );

        CommitGH.CommentsConnection comments = new CommitGH.CommentsConnection(
                List.of(comment)
        );

        CommitFile file = new CommitFile(
                "test.java",
                10,
                5,
                15,
                "modified",
                new URI("http://raw.url"),
                new URI("http://blob.url"),
                "patch content",
                null,
                "abc123",
                new URI("http://contents.url")
        );

        CommitGH commit = new CommitGH(
                "123",
                "abc123",
                commitUrl,
                committedDate,
                committedDate,
                1,
                10,
                5,
                commitMessage,
                comments,
                null,
                List.of(file)
        );

        // Act
        Commit result = mapper.toDomain(commit, userLogin, new NameWithOwner("owner", "repo"));

        // Assert
        assertNotNull(result);
        assertNull(result.getUser().id());
        assertNull(result.getRepository().id());
        assertEquals(userLogin, result.getUser().login());
        assertEquals("owner", result.getRepository().owner());
        assertEquals("repo", result.getRepository().name());
        assertEquals("123", result.getGithub().id());
        assertEquals(commitUrl, result.getGithub().url());
        assertEquals(committedDate, result.getCommittedDate());
        assertEquals(10, result.getAdditions());
        assertEquals(5, result.getDeletions());
        assertEquals(1, result.getCommentsCount());
        assertEquals(1, result.getFilesCount());

        // Verify comment mapping
        assertFalse(result.getComments().isEmpty());
        CommitComment mappedComment = result.getComments().getFirst();
        assertEquals("commenter", mappedComment.author());
        assertEquals("Test comment", mappedComment.body());

        // Verify file mapping
        assertFalse(result.getFiles().isEmpty());
        File mappedFile = result.getFiles().getFirst();
        assertEquals("test.java", mappedFile.fileName());
        assertEquals("test", mappedFile.baseName());
        assertEquals("java", mappedFile.extension());
        assertEquals(10, mappedFile.additions());
        assertEquals(5, mappedFile.deletions());
    }

    @Test
    @DisplayName("Should handle null collections properly")
    void shouldHandleNullCollections() throws Exception {
        // Arrange
        CommitGH commit = new CommitGH(
                "123",
                "abc123",
                new URI("https://github.com/test/commit/123"),
                LocalDate.now(),
                null,
                0,
                0,
                0,
                "Test commit",
                new CommitGH.CommentsConnection(null), // Null comments list
                null, // Null PRs
                null  // Null files
        );

        // Act
        Commit result = mapper.toDomain(commit, "testUser", new NameWithOwner("owner", "repo"));

        // Assert
        assertNotNull(result);
        assertTrue(result.getComments().isEmpty());
        assertTrue(result.getFiles().isEmpty());
        assertTrue(result.getAssociatedPullRequest().isEmpty());
        assertEquals(0, result.getCommentsCount());
        assertEquals(0, result.getFilesCount());
        assertEquals(0, result.getAssociatedPullRequestsCount());
    }

    @Test
    @DisplayName("Should correctly extract file basename and extension")
    void shouldExtractFileNameComponents() {
        // Arrange
        CommitFile file = new CommitFile(
                "src/main/java/com/example/Test.java",
                0, 0, 0, "modified", null, null, null, null, "abc123", null
        );

        // Act
        File result = mapper.map(file);

        // Assert
        assertNotNull(result);
        assertEquals("Test", result.baseName());
        assertEquals("java", result.extension());
        assertEquals("src/main/java/com/example/Test.java", result.path());
    }

    @Test
    @DisplayName("Should handle files without extensions")
    void shouldHandleFilesWithoutExtension() {
        // Arrange
        CommitFile file = new CommitFile(
                "README",
                0, 0, 0, "modified", null, null, null, null, "abc123", null
        );

        // Act
        File result = mapper.map(file);

        // Assert
        assertNotNull(result);
        assertEquals("README", result.baseName());
        assertEquals("", result.extension());
        assertEquals("README", result.path());
    }

    @Test
    @DisplayName("Should map associated pull requests correctly")
    void shouldMapAssociatedPullRequests() throws Exception {
        // Arrange
        CommitGH.PullRequestRef pr = new CommitGH.PullRequestRef(
                "pr123",
                new URI("https://github.com/test/pull/1")
        );
        CommitGH.AssociatedPullRequests prs = new CommitGH.AssociatedPullRequests(List.of(pr));

        CommitGH commit = createMinimalCommit();
        commit = new CommitGH(
                commit.id(),
                commit.oid(),
                commit.commitUrl(),
                commit.committedDate(),
                commit.pushedDate(),
                commit.changedFiles(),
                commit.additions(),
                commit.deletions(),
                commit.message(),
                commit.comments(),
                prs,
                commit.files()
        );

        // Act
        Commit result = mapper.toDomain(commit, "testUser", new NameWithOwner("owner", "repo"));

        // Assert
        assertNotNull(result);
        assertFalse(result.getAssociatedPullRequest().isEmpty());
        Github mappedPr = result.getAssociatedPullRequest().getFirst();
        assertEquals("pr123", mappedPr.id());
        assertEquals(new URI("https://github.com/test/pull/1"), mappedPr.url());
    }

    private CommitGH createMinimalCommit() throws Exception {
        return new CommitGH(
                "123",
                "abc123",
                new URI("https://github.com/test/commit/123"),
                LocalDate.now(),
                null,
                0,
                0,
                0,
                "Test commit",
                new CommitGH.CommentsConnection(List.of()),
                null,
                List.of()
        );
    }
}