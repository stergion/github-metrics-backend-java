package com.stergion.githubbackend.infrastructure.external.githubservice.client.mappers;

import com.stergion.githubbackend.domain.repositories.RepositoryDTO;
import com.stergion.githubbackend.domain.utils.types.Language;
import com.stergion.githubbackend.domain.utils.types.Topic;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.RepositoryGH;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.LabelsConnection;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.LabelNode;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.RepositoryOwner;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class RepositoryGHMapperTest {

    @Inject
    RepositoryGHMapper mapper;

    @Test
    @DisplayName("Should map all fields correctly when converting from RepositoryGH to DTO")
    void shouldMapAllFieldsCorrectly() {
        // Arrange
        var repositoryGH = createFullRepositoryGH();

        // Act
        RepositoryDTO dto = mapper.toDTO(repositoryGH);

        // Assert
        assertNotNull(dto);
        assertEquals("testOwner", dto.owner());
        assertEquals("testRepo", dto.name());
        assertEquals("test-id", dto.github().id());
        assertEquals(URI.create("https://example.com"), dto.github().url());
        assertEquals(100, dto.stargazerCount());
        assertEquals(50, dto.forkCount());
        assertEquals("Java", dto.primaryLanguage());
        assertEquals(2, dto.languages().size());
        assertEquals(2, dto.topics().size());
    }

    @Test
    @DisplayName("Should handle null collections properly")
    void shouldHandleNullCollections() {
        // Arrange
        var repositoryGH = createMinimalRepositoryGH();

        // Act
        RepositoryDTO dto = mapper.toDTO(repositoryGH);

        System.out.println(dto);

        // Assert
        assertNotNull(dto);
        assertNotNull(dto.languages(), "Languages list should not be null");
        assertNotNull(dto.topics(), "Topics list should not be null");
        assertNotNull(dto.labels(), "Labels list should not be null");
        assertTrue(dto.languages().isEmpty(), "Languages list should be empty");
        assertTrue(dto.topics().isEmpty(), "Topics list should be empty");
        assertTrue(dto.labels().isEmpty(), "Labels list should be empty");
    }

    @Test
    @DisplayName("Should calculate language percentages correctly")
    void shouldCalculateLanguagePercentagesCorrectly() {
        // Arrange
        var repositoryGH = createRepositoryWithLanguages();

        // Act
        RepositoryDTO dto = mapper.toDTO(repositoryGH);

        // Assert
        List<Language> languages = dto.languages();
        assertEquals(2, languages.size());

        Language java = languages.stream()
                                 .filter(l -> l.name().equals("Java"))
                                 .findFirst()
                                 .orElseThrow();
        Language python = languages.stream()
                                   .filter(l -> l.name().equals("Python"))
                                   .findFirst()
                                   .orElseThrow();

        assertEquals(75.0f, java.percentage(), 0.1);
        assertEquals(25.0f, python.percentage(), 0.1);
    }

    @Test
    @DisplayName("Should map topics correctly")
    void shouldMapTopicsCorrectly() {
        // Arrange
        var repositoryGH = createRepositoryWithTopics();

        // Act
        RepositoryDTO dto = mapper.toDTO(repositoryGH);

        // Assert
        List<Topic> topics = dto.topics();
        assertEquals(2, topics.size());
        assertTrue(topics.stream().anyMatch(t -> t.name().equals("java")));
        assertTrue(topics.stream().anyMatch(t -> t.name().equals("testing")));
    }

    private RepositoryGH createFullRepositoryGH() {
        return new RepositoryGH(
                "test-id",
                "testRepo",
                URI.create("https://example.com"),
                new RepositoryOwner("testOwner"),
                100,
                50,
                new RepositoryGH.LanguageInfo("Java"),
                createLanguagesConnection(),
                createLabelsConnection(),
                createTopicsConnection(),
                new RepositoryGH.WatchersConnection(25)
        );
    }

    private RepositoryGH createMinimalRepositoryGH() {
        return new RepositoryGH(
                "test-id",
                "testRepo",
                URI.create("https://example.com"),
                new RepositoryOwner("testOwner"),
                0,
                0,
                null,
                new RepositoryGH.LanguagesConnection(0, 0, List.of()),
                null,
                null,
                new RepositoryGH.WatchersConnection(0)
        );
    }

    private RepositoryGH.LanguagesConnection createLanguagesConnection() {
        return new RepositoryGH.LanguagesConnection(
                2,
                1000,
                List.of(
                        new RepositoryGH.LanguageEdge(
                                750,
                                new RepositoryGH.LanguageInfo("Java")
                        ),
                        new RepositoryGH.LanguageEdge(
                                250,
                                new RepositoryGH.LanguageInfo("Python")
                        )
                       )
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

    private RepositoryGH.RepositoryTopicsConnection createTopicsConnection() {
        return new RepositoryGH.RepositoryTopicsConnection(
                2,
                List.of(
                        new RepositoryGH.RepositoryTopic(
                                new RepositoryGH.TopicNode("java")
                        ),
                        new RepositoryGH.RepositoryTopic(
                                new RepositoryGH.TopicNode("testing")
                        )
                       )
        );
    }

    private RepositoryGH createRepositoryWithLanguages() {
        return new RepositoryGH(
                "test-id",
                "testRepo",
                URI.create("https://example.com"),
                new RepositoryOwner("testOwner"),
                0,
                0,
                new RepositoryGH.LanguageInfo("Java"),
                createLanguagesConnection(),
                null,
                null,
                new RepositoryGH.WatchersConnection(0)
        );
    }

    private RepositoryGH createRepositoryWithTopics() {
        return new RepositoryGH(
                "test-id",
                "testRepo",
                URI.create("https://example.com"),
                new RepositoryOwner("testOwner"),
                0,
                0,
                null,
                new RepositoryGH.LanguagesConnection(0, 0, List.of()),
                null,
                createTopicsConnection(),
                new RepositoryGH.WatchersConnection(0)
        );
    }
}