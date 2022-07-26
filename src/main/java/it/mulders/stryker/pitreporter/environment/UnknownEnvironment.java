package it.mulders.stryker.pitreporter.environment;

public class UnknownEnvironment implements Environment {
    private static final String unknown = "--unknown--";

    @Override
    public String getApiKey() {
        return unknown;
    }

    @Override
    public String getProjectName() {
        return unknown;
    }

    @Override
    public String getProjectVersion() {
        return unknown;
    }
}
