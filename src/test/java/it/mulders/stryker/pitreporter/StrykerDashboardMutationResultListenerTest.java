package it.mulders.stryker.pitreporter;

import it.mulders.stryker.pitreporter.dashboard.client.StrykerDashboardClient;
import it.mulders.stryker.pitreporter.dashboard.client.StrykerDashboardClientException;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.pitest.classinfo.ClassName;
import org.pitest.classpath.CodeSource;
import org.pitest.classpath.ClassFilter;
import org.pitest.classpath.ClassPath;
import org.pitest.classpath.PathFilter;
import org.pitest.classpath.ProjectClassPaths;
import org.pitest.elements.utils.JsonParser;
import org.pitest.mutationtest.ClassMutationResults;
import org.pitest.mutationtest.DetectionStatus;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.MutationStatusTestPair;
import org.pitest.mutationtest.engine.Location;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.engine.MutationIdentifier;
import org.pitest.util.PitError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StrykerDashboardMutationResultListenerTest implements WithAssertions {
    private final CodeSource codeSource = new CodeSource(
            new ProjectClassPaths(
                    new ClassPath(),
                    new ClassFilter(x -> true, x -> true),
                    new PathFilter(x -> true, x -> true)
            )
    );
    private final JsonParser jsonParser = new JsonParser(Collections.emptyList());
    private final TestableStrykerDashboardClient dashboardClient = new TestableStrykerDashboardClient();
    private final StrykerDashboardMutationResultListener listener = new StrykerDashboardMutationResultListener(
            codeSource,
            null,
            jsonParser,
            dashboardClient
    );

    private final MutationDetails mutationDetails = new MutationDetails(
            new MutationIdentifier(
                    new Location(ClassName.fromClass(StrykerDashboardMutationResultListenerTest.class), "nonExisting", "nonExisting()/V"),
                    0,
                    "some-mutation"
            ),
            "it.mulders.stryker.pitreporter/StrykerDashboardMutationResultListenerTest.java",
            "Some mutation",
            25,
            1
    );

    @Test
    void should_collect_package_info() {
        // Arrange
        var mstp = new MutationStatusTestPair(1, DetectionStatus.KILLED, null, null);
        var mutation = new MutationResult(mutationDetails, mstp);
        var cmr = new ClassMutationResults(Collections.singleton(mutation));

        // Act
        listener.handleMutationResult(cmr);

        // Assert
        assertThat(listener.getPackageSummaryData().valuesList())
                .hasSize(1)
                .allSatisfy(psd -> {
                    assertThat(psd.getSummaryData())
                            .hasSize(1)
                            .allSatisfy(sd -> {
                                assertThat(sd.getPackageName()).isEqualTo("it/mulders/stryker/pitreporter");
                            });
        });
    }

    @Test
    void should_upload_result() {
        // Arrange
        var mstp = new MutationStatusTestPair(1, DetectionStatus.KILLED, null, null);
        var mutation = new MutationResult(mutationDetails, mstp);
        var cmr = new ClassMutationResults(Collections.singleton(mutation));

        // Act
        listener.handleMutationResult(cmr);
        listener.runEnd();

        // Assert
        assertThat(dashboardClient.uploadedReports)
                .hasSize(1)
                .allSatisfy(report -> {
                    assertThat(report.moduleName).isNull();
                });
    }

    @Test
    void should_fail_when_upload_fails() {
        // Arrange
        var mstp = new MutationStatusTestPair(1, DetectionStatus.KILLED, null, null);
        var mutation = new MutationResult(mutationDetails, mstp);
        var cmr = new ClassMutationResults(Collections.singleton(mutation));
        var error = "Oh noes, an error";
        var listener = new StrykerDashboardMutationResultListener(
                codeSource,
                null,
                jsonParser,
                new StrykerDashboardClient(null) {
                    @Override
                    public void uploadReport(String report, String moduleName) throws StrykerDashboardClientException {
                        throw new StrykerDashboardClientException(error);
                    }
                }
        );

        // Act
        listener.handleMutationResult(cmr);

        // Assert
        assertThatThrownBy(() -> listener.runEnd())
                .isInstanceOf(PitError.class)
                .cause()
                .hasMessageContaining(error);
    }

    static class TestableStrykerDashboardClient extends StrykerDashboardClient {
        final List<Report> uploadedReports = new ArrayList<>();

        TestableStrykerDashboardClient() {
            super(null);
        }

        @Override
        public void uploadReport(final String report, final String moduleName) {
            uploadedReports.add(new Report(moduleName, report));
        }
    }

    static class Report {
        final String moduleName;
        final String content;

        public Report(final String moduleName, final String content) {
            this.moduleName = moduleName;
            this.content = content;
        }
    }
}