package it.mulders.stryker.pitreporter.environment;

/**
 * Describes an environment where PIT can be run, and how this reporter
 * could obtain information that it needs to properly report the mutation
 * testing results to the Stryker Dashboard.
 */
public interface Environment {
    /** Should return the Stryker Dashboard API key */
    String getApiKey();

    /** Should return the project name for reporting to the Stryker Dashboard */
    String getProjectName();

    /** Should return the project version for reporting to the Stryker Dashboard */
    String getProjectVersion();
}
