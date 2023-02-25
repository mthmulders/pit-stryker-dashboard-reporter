package it.mulders.stryker.pitreporter.dashboard.client;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import it.mulders.stryker.pitreporter.environment.Environment;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.unauthorized;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WireMockTest
class StrykerDashboardClientIT implements WithAssertions {
    @Nested
    class RequestConstructionIT {
        @BeforeEach
        void prepare_stub() {
            stubFor(put("/api/reports/test.com/octocat/hello-world/main")
                    .willReturn(ok().withBody("")));
        }

        @Test
        void should_include_API_key(final WireMockRuntimeInfo wmRuntimeInfo) throws StrykerDashboardClientException {
            // Arrange
            var client = new StrykerDashboardClient(new TestEnvironment(), wmRuntimeInfo.getHttpBaseUrl());

            // Act
            client.uploadReport("", null);

            // Assert
            verify(
                putRequestedFor(urlMatching("/api/reports/.*/.*"))
                        .withHeader("X-Api-Key", equalTo("336286b3-0131-45da-94fe-75f76041d566"))
            );
        }

        @Test
        void should_construct_correct_URL_without_module(final WireMockRuntimeInfo wmRuntimeInfo) throws StrykerDashboardClientException {
            // Arrange
            var client = new StrykerDashboardClient(new TestEnvironment(), wmRuntimeInfo.getHttpBaseUrl());

            // Act
            client.uploadReport("", null);

            // Assert
            verify(
                    putRequestedFor(urlEqualTo("/api/reports/test.com/octocat/hello-world/main"))
            );
        }

        @Test
        void should_construct_correct_URL_with_module(final WireMockRuntimeInfo wmRuntimeInfo) throws StrykerDashboardClientException {
            // Arrange
            var client = new StrykerDashboardClient(new TestEnvironment(), wmRuntimeInfo.getHttpBaseUrl());
            stubFor(put("/api/reports/test.com/octocat/hello-world/main?module=foo")
                    .willReturn(ok().withBody("")));

            // Act
            client.uploadReport("", "foo");

            // Assert
            verify(
                    putRequestedFor(urlEqualTo("/api/reports/test.com/octocat/hello-world/main?module=foo"))
            );
        }
    }

    @Nested
    class ResponseHandlingIT {
        @Test
        void should_handle_200_response(final WireMockRuntimeInfo wmRuntimeInfo) throws StrykerDashboardClientException {
            // Arrange
            var client = new StrykerDashboardClient(new TestEnvironment(), wmRuntimeInfo.getHttpBaseUrl());
            stubFor(put("/api/reports/test.com/octocat/hello-world/main")
                    .willReturn(ok().withBody("")));

            // Act
            client.uploadReport("", null);

            // Assert
        }

        @Test
        void should_handle_401_response(final WireMockRuntimeInfo wmRuntimeInfo) {
            // Arrange
            var client = new StrykerDashboardClient(new TestEnvironment(), wmRuntimeInfo.getHttpBaseUrl());
            stubFor(put("/api/reports/test.com/octocat/hello-world/main")
                    .willReturn(unauthorized().withBody("")));

            // Act
            assertThatThrownBy(() -> client.uploadReport("", null))
                    .isInstanceOf(StrykerDashboardClientException.class)
                    .hasMessageContaining("API key");

            // Assert
        }

        @Test
        void should_handle_404_response(final WireMockRuntimeInfo wmRuntimeInfo) {
            // Arrange
            var client = new StrykerDashboardClient(new TestEnvironment(), wmRuntimeInfo.getHttpBaseUrl());
            stubFor(put("/api/reports/test.com/octocat/hello-world/main")
                    .willReturn(notFound().withBody("")));

            // Act
            assertThatThrownBy(() -> client.uploadReport("", null))
                    .isInstanceOf(StrykerDashboardClientException.class)
                    .hasMessageContaining("dashboard registration");

            // Assert
        }
    }

    static class TestEnvironment implements Environment {
        @Override
        public String getApiKey() {
            return "336286b3-0131-45da-94fe-75f76041d566";
        }

        @Override
        public String getProjectName() {
            return "test.com/octocat/hello-world";
        }

        @Override
        public String getProjectVersion() {
            return "main";
        }
    }
}