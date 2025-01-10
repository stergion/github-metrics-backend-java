package com.stergion.githubbackend.infrastructure.external.githubservice.service;

import com.stergion.githubbackend.domain.users.UserDTO;
import com.stergion.githubbackend.domain.utils.types.Github;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.GitHubServiceClient;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.exceptions.NotGithubUserException;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.exceptions.RequestParamsValidationException;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.mappers.UserGHMapper;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.UserGH;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class UserClientTest {

    @InjectMock
    @RestClient
    GitHubServiceClient gitHubServiceClient;

    @InjectMock
    UserGHMapper mapper;

    @Inject
    UserClient userClient;

    private static final String VALID_LOGIN = "testuser";
    private static final String INVALID_LOGIN = "!!!invalidlogin!!!";
    private static final String USER_ID = "1234";
    private static final URI GITHUB_URL = URI.create("https://github.com/" + VALID_LOGIN);
    private static final URI AVATAR_URL = URI.create(
            "https://avatars.githubusercontent.com/u/1234");
    private static final URI WEBSITE_URL = URI.create("https://example.com");

    private UserGH mockUserGH;
    private UserDTO mockUserDTO;
    private Github githubRef;

    @BeforeEach
    void setup() {
        // Initialize common references
        githubRef = new Github(USER_ID, GITHUB_URL);

        // Initialize mock response objects
        mockUserGH = new UserGH(
                USER_ID,
                VALID_LOGIN,
                "Test User",
                "Test bio",
                GITHUB_URL,
                "test@example.com",
                AVATAR_URL,
                "testhandle",
                WEBSITE_URL
        );

        mockUserDTO = new UserDTO(
                null,
                VALID_LOGIN,
                "Test User",
                githubRef,
                "test@example.com",
                List.of(),
                AVATAR_URL,
                "Test bio",
                "testhandle",
                WEBSITE_URL
        );
    }

    @Test
    @DisplayName("Should return user info for valid login")
    void shouldReturnUserInfoForValidLogin() {
        when(gitHubServiceClient.getUserInfo(VALID_LOGIN)).thenReturn(mockUserGH);
        when(mapper.toDTO(mockUserGH)).thenReturn(mockUserDTO);

        UserDTO result = userClient.getUserInfo(VALID_LOGIN);

        assertNotNull(result);
        assertEquals(VALID_LOGIN, result.login());
        assertEquals("Test User", result.name());
        assertEquals("test@example.com", result.email());
        assertEquals(githubRef, result.github());
        assertEquals(AVATAR_URL, result.avatarURL());
        assertEquals("Test bio", result.bio());
        assertEquals("testhandle", result.twitterHandle());
        assertEquals(WEBSITE_URL, result.websiteURL());

        verify(gitHubServiceClient).getUserInfo(VALID_LOGIN);
        verify(mapper).toDTO(mockUserGH);
    }

    @Test
    @DisplayName("Should throw NotGithubUserException when user doesn't exist")
    void shouldThrowNotGithubUserExceptionWhenUserDoesntExist() {
        when(gitHubServiceClient.getUserInfo(VALID_LOGIN))
                .thenThrow(new NotGithubUserException("User not found"));

        NotGithubUserException exception = assertThrows(
                NotGithubUserException.class,
                () -> userClient.getUserInfo(VALID_LOGIN)
                                                       );
        assertEquals("User not found", exception.getMessage());
        verify(gitHubServiceClient).getUserInfo(VALID_LOGIN);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Should throw RequestParamsValidationException for invalid login")
    void shouldThrowRequestParamsValidationExceptionForInvalidLogin() {
        List<RequestParamsValidationException.ValidationError> errors = List.of(
                new RequestParamsValidationException.ValidationError(
                        INVALID_LOGIN,
                        "GitHub username can only contain alphanumeric characters and hyphens",
                        "params",
                        "login"
                )
                                                                               );

        when(gitHubServiceClient.getUserInfo(INVALID_LOGIN))
                .thenThrow(new RequestParamsValidationException("Invalid parameters", errors));

        RequestParamsValidationException exception = assertThrows(
                RequestParamsValidationException.class,
                () -> userClient.getUserInfo(INVALID_LOGIN)
                                                                 );
        assertEquals("Invalid parameters", exception.getMessage());
        assertEquals(1, exception.getValidationErrors().size());
        assertEquals("GitHub username can only contain alphanumeric characters and hyphens",
                exception.getValidationErrors().getFirst().message());
        verify(gitHubServiceClient).getUserInfo(INVALID_LOGIN);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Should handle null fields in user response")
    void shouldHandleNullFieldsInUserResponse() {
        UserGH userGHWithNulls = new UserGH(
                USER_ID,
                VALID_LOGIN,
                null,
                null,
                GITHUB_URL,
                "test@example.com",
                AVATAR_URL,
                null,
                null
        );

        UserDTO userDTOWithNulls = new UserDTO(
                null,
                VALID_LOGIN,
                VALID_LOGIN,
                githubRef,
                "test@example.com",
                List.of(),
                AVATAR_URL,
                null,
                null,
                null
        );

        when(gitHubServiceClient.getUserInfo(VALID_LOGIN)).thenReturn(userGHWithNulls);
        when(mapper.toDTO(userGHWithNulls)).thenReturn(userDTOWithNulls);

        UserDTO result = userClient.getUserInfo(VALID_LOGIN);

        assertNotNull(result);
        assertEquals(VALID_LOGIN, result.login());
        assertEquals(VALID_LOGIN, result.name());
        assertEquals(githubRef, result.github());
        assertEquals(AVATAR_URL, result.avatarURL());
        assertNull(result.bio());
        assertNull(result.twitterHandle());
        assertNull(result.websiteURL());

        verify(gitHubServiceClient).getUserInfo(VALID_LOGIN);
        verify(mapper).toDTO(userGHWithNulls);
    }
}