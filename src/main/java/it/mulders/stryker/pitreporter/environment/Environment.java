package it.mulders.stryker.pitreporter.environment;

/**
 * Describes an environment where PIT can be run, and how this reporter
 * could obtain information that it needs to properly report the mutation
 * testing results to the Stryker Dashboard.
 */
public interface Environment {
    /**
     * Should return the Stryker Dashboard API key.
     * Typically, this looks like a <strong>UUID</strong>.
     * @return A valid Stryker Dashboard API key.
     */
    String getApiKey();

    /**
     * Should return the project name for reporting to the Stryker Dashboard.
     * Typically, something like <code>provider/owner/repository</code>.
     * @return The name under which the report should be uploaded to the Stryker Dashboard.
     */
    String getProjectName();

    /**
     * Should return the project version for reporting to the Stryker Dashboard.
     * Typically, the name of the branch or tag that you're reporting upon.
     * @return  The version under which the report should be uploaded to the Stryker Dashboard.
     */
    String getProjectVersion();
}
