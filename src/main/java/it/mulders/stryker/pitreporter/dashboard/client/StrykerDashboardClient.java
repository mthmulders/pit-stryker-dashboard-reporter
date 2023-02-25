package it.mulders.stryker.pitreporter.dashboard.client;

import it.mulders.stryker.pitreporter.environment.Environment;
import org.pitest.util.Log;
import org.pitest.util.StringUtil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client for uploading mutation testing reports to the Stryker Dashboard.
 */
public class StrykerDashboardClient {
    private static final Logger log = Log.getLogger();

    private final HttpClient client = HttpClient.newHttpClient();
    private final Environment environment;
    private final String hostname;

    /**
     * Constructor.
     * @param environment How to obtain information about the project from the environment.
     */
    public StrykerDashboardClient(final Environment environment) {
        this(environment, "https://dashboard.stryker-mutator.io");
    }

    // Visible for testing
    StrykerDashboardClient(final Environment environment, final String hostname) {
        this.environment = environment;
        this.hostname = hostname;
    }

    /**
     * Upload a report.
     * @param report The JSON report to upload.
     * @param moduleName Name of the project module for the report.
     */
    public void uploadReport(final String report, final String moduleName) throws StrykerDashboardClientException {
        var uri = constructReportUploadUri(moduleName);
        log.log(Level.INFO, "Uploading report to {0}", uri);

        var apiKey = environment.getApiKey();
        if (log.isLoggable(Level.CONFIG)) {
            log.log(Level.CONFIG, "Using API key {0}{1}", new Object[] {
                    StringUtil.repeat('*', apiKey.length() - 3),
                    apiKey.substring(apiKey.length() - 3)
            });
        }

        var body = HttpRequest.BodyPublishers.ofString(report);

        var request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .header("X-Api-Key", apiKey)
                .uri(URI.create(uri))
                .PUT(body)
                .build();

        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            var statusCode = response.statusCode();

            switch (statusCode) {
                case 200:
                    log.log(Level.INFO, "Successfully uploaded report");
                    return;
                case 401:
                    log.log(Level.SEVERE, "Failed to upload report, please check your API key!");
                    logResponseBody(response);
                    throw new StrykerDashboardClientException("Please check your API key!");
                case 404:
                    log.log(Level.SEVERE, "Failed to upload report, please check your dashboard registration!");
                    logResponseBody(response);
                    throw new StrykerDashboardClientException("Please check your dashboard registration!");
                default:
                    log.log(Level.SEVERE, "Unexpected response status: {0}", statusCode);
                    logResponseBody(response);
                    throw new StrykerDashboardClientException("Unknown error, please check the logs with --verbose before raising an issue");
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "I/O error when sending the report or receiving the answer", e);
        } catch (InterruptedException e) {
            log.log(Level.SEVERE, "Sending the report or receiving the answer was interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    private void logResponseBody(HttpResponse<String> response) {
        log.log(Level.FINE, "API returned {0}", response.body());
    }

    @SuppressWarnings("java:S125") // The inline comment with HTTP request is not code
    private String constructReportUploadUri(final String moduleName) {
        // PUT ${BASE_URL}/api/reports/${PROJECT}/${VERSION}?module=${MODULE}
        // -H 'Content-Type: application/json' \
        // -H 'Host: dashboard.stryker-mutator.io' \
        // -H "X-Api-Key: ${API_KEY}" \
        var projectName = environment.getProjectName();
        var projectVersion = environment.getProjectVersion();

        var uri = String.format("%s/api/reports/%s/%s",hostname, projectName, projectVersion);
        if (moduleName != null) uri += String.format("?module=%s", moduleName);

        return uri;
    }
}
