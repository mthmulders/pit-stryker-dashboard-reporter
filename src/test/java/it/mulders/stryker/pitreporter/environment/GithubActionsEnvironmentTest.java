package it.mulders.stryker.pitreporter.environment;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Map;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GithubActionsEnvironmentTest implements WithAssertions {
    private final Map<String, String> env = Map.of(
            "GITHUB_REPOSITORY", "octocat/Hello-World",
            "STRYKER_DASHBOARD_API_KEY", "140cad54-8c61-4393-ac53-54b1f2d454ed",
            "GITHUB_REF_NAME", "feature-branch-1"
    );

    private final Environment environment = new GithubActionsEnvironment(env);

    @Test
    void should_build_project_name() {
        assertThat(environment.getProjectName()).isEqualTo("github.com/octocat/Hello-World");
    }

    @Test
    void should_return_STRYKER_DASHBOARD_API_KEY() {
        assertThat(environment.getApiKey()).isEqualTo("140cad54-8c61-4393-ac53-54b1f2d454ed");
    }

    @Test
    void should_return_GITHUB_REF_NAME() {
        assertThat(environment.getProjectVersion()).isEqualTo("feature-branch-1");
    }
}