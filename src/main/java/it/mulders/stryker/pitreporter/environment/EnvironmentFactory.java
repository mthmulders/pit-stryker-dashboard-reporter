package it.mulders.stryker.pitreporter.environment;

import org.pitest.util.Log;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnvironmentFactory {
    private static final Logger log = Log.getLogger();

    public static Environment findEnvironment() {
        return findEnvironment(System.getenv());
    }

    // Visible for testing
    static Environment findEnvironment(final Map<String, String> environment) {
        if (environment.getOrDefault("GITHUB_ACTIONS", "false").equals("true")) {
            return new GithubActionsEnvironment();
        }

        log.log(Level.SEVERE, () -> "Could not reliably determine your environment, this reporter will not work correctly.");
        log.log(Level.SEVERE, () -> "Consider reporting an issue at https://github.com/mthmulders/pit-stryker-dashboard-reporter/issues/new.");
        return new UnknownEnvironment();
    }
}
