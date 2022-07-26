package it.mulders.stryker.pitreporter.environment;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UnknownEnvironmentTest implements WithAssertions {
    private final Environment environment = new UnknownEnvironment();

    @Test
    void should_build_project_name() {
        assertThat(environment.getProjectName()).isEqualTo("--unknown--");
    }

    @Test
    void should_return_STRYKER_DASHBOARD_API_KEY() {
        assertThat(environment.getApiKey()).isEqualTo("--unknown--");
    }

    @Test
    void should_return_GITHUB_REF_NAME() {
        assertThat(environment.getProjectVersion()).isEqualTo("--unknown--");
    }
}