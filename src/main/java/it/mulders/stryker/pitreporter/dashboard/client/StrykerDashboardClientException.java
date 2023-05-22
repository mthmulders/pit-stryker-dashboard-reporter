package it.mulders.stryker.pitreporter.dashboard.client;

/**
 * Signals insufficient or incorrect configuration for uploading a report to the Stryker dashboard.
 */
public class StrykerDashboardClientException extends Exception {
    /**
     * Create a new instance.
     * @param message Detailed information on what is missing or what is probably incorrect.
     */
    public StrykerDashboardClientException(String message) {
        super(message);
    }
}
