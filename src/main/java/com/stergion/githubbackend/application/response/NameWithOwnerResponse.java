package com.stergion.githubbackend.application.response;

import jakarta.validation.constraints.NotBlank;

public record NameWithOwnerResponse(
        @NotBlank
        String owner,
        @NotBlank
        String name
) {
}
