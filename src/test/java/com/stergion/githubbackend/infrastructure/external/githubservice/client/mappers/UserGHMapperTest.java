package com.stergion.githubbackend.infrastructure.external.githubservice.client.mappers;

import com.stergion.githubbackend.domain.users.User;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.UserGH;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class UserGHMapperTest {

    @Inject
    UserGHMapper mapper;

    @Test
    void testMapping() throws Exception {
        // Create test UserGH
        UserGH userGH = new UserGH(
                "123",
                "testUser",
                "Test User",
                "Test bio",
                new URI("https://github.com/testUser"),
                "test@example.com",
                new URI("https://avatar.url"),
                "testTwitter",
                new URI("https://website.url")
        );

        // Map to domain
        User result = mapper.toDomain(userGH);

        // Verify each field is mapped correctly
        assertEquals("testUser", result.login());
        assertEquals("Test User", result.name());
        assertEquals("123", result.github().id());
        assertEquals(new URI("https://github.com/testUser"), result.github().url());
        assertEquals("test@example.com", result.email());
        assertEquals(Collections.emptyList(), result.repositories());
        assertEquals(new URI("https://avatar.url"), result.avatarURL());
        assertEquals("Test bio", result.bio());
        assertEquals("testTwitter", result.twitterHandle());
        assertEquals(new URI("https://website.url"), result.websiteURL());
    }

    @Test
    void testNullHandling() throws Exception {
        UserGH userGH = new UserGH(
                "123",
                "testUser",
                null,  // name is null
                null,  // bio is null
                new URI("https://github.com/testUser"),
                "test@example.com",
                new URI("https://avatar.url"),
                null,  // twitter is null
                null   // website is null
        );

        User result = mapper.toDomain(userGH);

        assertEquals("testUser", result.login());
        assertNull(result.bio());
        assertNull(result.twitterHandle());
        assertNull(result.websiteURL());
    }
}