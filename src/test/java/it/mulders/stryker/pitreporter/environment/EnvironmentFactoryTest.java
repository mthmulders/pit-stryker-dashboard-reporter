package it.mulders.stryker.pitreporter.environment;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Map;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EnvironmentFactoryTest implements WithAssertions {
    @Test
    void should_detect_GitHub_Actions() {
        var env = Map.of("GITHUB_ACTIONS", "true");
        var result = EnvironmentFactory.findEnvironment(env);
        assertThat(result).isInstanceOf(GithubActionsEnvironment.class);
    }

    @Test
    void should_never_return_null() {
        assertThat(EnvironmentFactory.findEnvironment()).isNotNull();
        assertThat(EnvironmentFactory.findEnvironment(Map.of())).isNotNull();
    }
}