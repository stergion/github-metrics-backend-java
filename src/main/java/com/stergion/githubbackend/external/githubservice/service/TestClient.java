package com.stergion.githubbackend.external.githubservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stergion.githubbackend.external.githubservice.client.GitHubServiceClient;
import com.stergion.githubbackend.external.githubservice.client.models.TestDTO;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

@ApplicationScoped
public class TestClient {
    @RestClient
    GitHubServiceClient client;

    static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public Multi<TestDTO> getTest() {
        throw new NotImplementedYet();
//        return client.getTest()
//                     .onItem()
//                     .transform(Unchecked.function(t -> {
//                         try {
//                             return mapper.readValue(t, TestDTO.class);
//                         } catch (JsonProcessingException e) {
//                             throw new RuntimeException(e);
//                         }
//                     }));
    }
}
