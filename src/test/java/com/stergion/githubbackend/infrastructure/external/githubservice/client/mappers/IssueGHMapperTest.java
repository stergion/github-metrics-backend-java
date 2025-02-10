package com.stergion.githubbackend.infrastructure.external.githubservice.client.mappers;

import com.stergion.githubbackend.domain.contirbutions.models.IssueDTO;
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
        IssueDTO dto = mapper.toDTO(issueGH, TEST_LOGIN);

        // Then
        assertNotNull(dto);
        assertEquals(TEST_LOGIN, dto.user());
        assertEquals("test-repo", dto.repository().name());
        assertEquals("owner", dto.repository().owner());
        assertEquals("123", dto.github().id());
        assertEquals(URI.create("https://example.com"), dto.github().url());
        assertEquals(date, dto.createdAt());
        assertEquals(date, dto.updatedAt());
        assertEquals(date, dto.closedAt());
        assertEquals(IssueState.CLOSED, dto.state());
        assertEquals("Test Issue", dto.title());
        assertEquals("Test body", dto.body());
        assertEquals(5, dto.reactionsCount());
        assertEquals("closer", dto.closer());

        // Verify labels
        assertEquals(2, dto.labels().size());
        assertTrue(dto.labels().stream().anyMatch(l -> l.name().equals("bug")));
        assertTrue(dto.labels().stream().anyMatch(l -> l.name().equals("feature")));
        assertEquals("Bug description", dto.labels().stream()
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
        IssueDTO dto = mapper.toDTO(issueGH, TEST_LOGIN);

        // Then
        assertNotNull(dto);
        assertNotNull(dto.labels());
        assertTrue(dto.labels().isEmpty());
        assertNull(dto.closer());
        assertEquals(0, dto.reactionsCount());
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