package it.mulders.stryker.pitreporter.environment;

/**
 * Fallback-implementation of {@link Environment} that definitely does not produce useful information.
 */
public class UnknownEnvironment implements Environment {
    private static final String UNKNOWN = "--unknown--";

    @SuppressWarnings("java:S4144")
    @Override
    public String getApiKey() {
        return UNKNOWN;
    }

    @SuppressWarnings("java:S4144")
    @Override
    public String getProjectName() {
        return UNKNOWN;
    }

    @SuppressWarnings("java:S4144")
    @Override
    public String getProjectVersion() {
        return UNKNOWN;
    }
}
