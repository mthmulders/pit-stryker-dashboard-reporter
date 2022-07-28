package it.mulders.stryker.pitreporter;

import it.mulders.stryker.pitreporter.dashboard.client.StrykerDashboardClient;
import it.mulders.stryker.pitreporter.environment.EnvironmentFactory;

import org.pitest.coverage.CoverageDatabase;
import org.pitest.elements.models.MutationTestSummaryData;
import org.pitest.elements.models.PackageSummaryMap;
import org.pitest.elements.utils.JsonParser;
import org.pitest.mutationtest.ClassMutationResults;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.mutationtest.SourceLocator;
import org.pitest.util.Log;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Listener for {@link ClassMutationResults mutation results} emitted by PIT.
 */
public class StrykerDashboardMutationResultListener implements MutationResultListener {
    private static final Logger log = Log.getLogger();

    private final CoverageDatabase coverage;
    private final JsonParser jsonParser;
    private final PackageSummaryMap packageSummaryData = new PackageSummaryMap();
    private final StrykerDashboardClient dashboardClient;

    /**
     * Construct a new listener instance.
     * @param coverage A database of coverage.
     * @param locators Zero or more locators for source code.
     */
    public StrykerDashboardMutationResultListener(final CoverageDatabase coverage, final SourceLocator... locators) {
        this(
                coverage,
                new JsonParser(new HashSet<>(Arrays.asList(locators))),
                new StrykerDashboardClient(EnvironmentFactory.findEnvironment())
        );
    }

    // Visible for testing
    StrykerDashboardMutationResultListener(
            final CoverageDatabase coverage,
            final JsonParser jsonParser,
            final StrykerDashboardClient dashboardClient
    ) {
        this.coverage = coverage;
        this.jsonParser = jsonParser;
        this.dashboardClient = dashboardClient;
    }

    // Visible for testing
    PackageSummaryMap getPackageSummaryData() {
        return packageSummaryData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runStart() {
        // Nothing to do
    }

    private MutationTestSummaryData createSummaryData(
            final CoverageDatabase coverage,
            final ClassMutationResults data
    ) {
        return new MutationTestSummaryData(
                data.getFileName(),
                data.getMutations(),
                coverage.getClassInfo(Collections.singleton(data.getMutatedClass()))
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleMutationResult(final ClassMutationResults metaData) {
        var packageName = metaData.getPackageName();
        this.packageSummaryData.update(packageName, createSummaryData(this.coverage, metaData));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runEnd() {
        String json;
        try {
            json = jsonParser.toJson(this.packageSummaryData);
        } catch (IOException ioe) {
            log.log(Level.SEVERE, "Could not convert PIT results to Stryker Dashboard format", ioe);
            return;
        }

        try(var reader = new StringReader(json);
            var stream = new ReaderInputStream(reader)) {
            dashboardClient.uploadReport(stream);
        } catch (IOException ioe) {
            log.log(Level.SEVERE, "Could not close internal input stream", ioe);
        }
    }

}
