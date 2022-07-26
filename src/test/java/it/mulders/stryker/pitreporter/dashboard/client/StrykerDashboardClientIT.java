package it.mulders.stryker.pitreporter.dashboard.client;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import it.mulders.stryker.pitreporter.environment.Environment;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WireMockTest
class StrykerDashboardClientIT implements WithAssertions {
    @Test
    void should_include_API_key(final WireMockRuntimeInfo wmRuntimeInfo) {
        // Arrange
        var client = new StrykerDashboardClient(new TestEnvironment(), wmRuntimeInfo.getHttpBaseUrl());

        // Act
        client.uploadReport(new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)));

        // Assert
        verify(
            putRequestedFor(urlMatching("/api/reports/.*/.*"))
                    .withHeader("X-Api-Key", equalTo("336286b3-0131-45da-94fe-75f76041d566"))
        );
    }

    @Test
    void should_construct_correct_URL(final WireMockRuntimeInfo wmRuntimeInfo) {
        // Arrange
        var client = new StrykerDashboardClient(new TestEnvironment(), wmRuntimeInfo.getHttpBaseUrl());

        // Act
        client.uploadReport(new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)));

        // Assert
        verify(
                putRequestedFor(urlEqualTo("/api/reports/octocat/hello-world/main"))
        );
    }

    static class TestEnvironment implements Environment {
        @Override
        public String getApiKey() {
            return "336286b3-0131-45da-94fe-75f76041d566";
        }

        @Override
        public String getProjectName() {
            return "octocat/hello-world";
        }

        @Override
        public String getProjectVersion() {
            return "main";
        }
    }
}