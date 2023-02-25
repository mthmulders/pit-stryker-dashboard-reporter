package it.mulders.stryker.pitreporter;

import it.mulders.stryker.pitreporter.dashboard.client.StrykerDashboardClientException;
import it.mulders.stryker.pitreporter.dashboard.client.StrykerDashboardClient;
import it.mulders.stryker.pitreporter.environment.EnvironmentFactory;

import org.pitest.classpath.CodeSource;
import org.pitest.elements.models.MutationTestSummaryData;
import org.pitest.elements.models.PackageSummaryMap;
import org.pitest.elements.utils.JsonParser;
import org.pitest.mutationtest.ClassMutationResults;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.mutationtest.SourceLocator;
import org.pitest.util.Log;
import org.pitest.util.PitError;

import java.io.IOException;
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

    private final CodeSource codeSource;
    private final String moduleName;
    private final JsonParser jsonParser;
    private final PackageSummaryMap packageSummaryData = new PackageSummaryMap();
    private final StrykerDashboardClient dashboardClient;

    /**
     * Construct a new listener instance.
     * @param codeSource Information about the source code on which PIT was executed.
     * @param moduleName The name of the module on which PIT was executed.
     * @param locators Zero or more locators for source code.
     */
    public StrykerDashboardMutationResultListener(
            final CodeSource codeSource,
            final String moduleName,
            final SourceLocator... locators)
    {
        this(
                codeSource,
                moduleName,
                new JsonParser(new HashSet<>(Arrays.asList(locators))),
                new StrykerDashboardClient(EnvironmentFactory.findEnvironment())
        );
    }

    // Visible for testing
    StrykerDashboardMutationResultListener(
            final CodeSource codeSource,
            final String moduleName,
            final JsonParser jsonParser,
            final StrykerDashboardClient dashboardClient
    ) {
        this.codeSource = codeSource;
        this.moduleName = moduleName;
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

    private MutationTestSummaryData createSummaryData(final ClassMutationResults classMutationResults) {
        var fileName = classMutationResults.getFileName();
        var mutations = classMutationResults.getMutations();
        var className = classMutationResults.getMutatedClass();
        var classInfo = codeSource.getClassInfo(Collections.singleton(className));

        return new MutationTestSummaryData(fileName, mutations, classInfo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleMutationResult(final ClassMutationResults metaData) {
        var packageName = metaData.getPackageName();
        this.packageSummaryData.update(packageName, createSummaryData(metaData));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runEnd() {
        try {
            var json = jsonParser.toJson(this.packageSummaryData);
            dashboardClient.uploadReport(json, moduleName);
        } catch (StrykerDashboardClientException ce) {
            throw new PitError("Failed to upload report", ce);
        } catch (IOException ioe) {
            log.log(Level.SEVERE, "Could not convert PIT results to Stryker Dashboard format", ioe);
        }
    }

}
