package it.mulders.stryker.pitreporter.environment;

import java.util.Map;

/**
 * Implementation of {@link Environment} that resolves all information from GitHub-provided environment variables.
 * Reference: <a href="https://docs.github.com/en/actions/learn-github-actions/environment-variables#default-environment-variables">Default environment variables</a>.
 */
public class GithubActionsEnvironment implements Environment {
    private final Map<String, String> environment;

    /**
     * Default constructor.
     */
    public GithubActionsEnvironment() {
        this(System.getenv());
    }

    // Visible for testing
    GithubActionsEnvironment(final Map<String, String> environment) {
        this.environment = environment;
    }

    @Override
    public String getApiKey() {
        return environment.get("STRYKER_DASHBOARD_API_KEY");
    }

    @Override
    public String getProjectName() {
        return String.format("github.com/%s", environment.get("GITHUB_REPOSITORY"));
    }

    @Override
    public String getProjectVersion() {
        return environment.get("GITHUB_REF_NAME");
    }
}
