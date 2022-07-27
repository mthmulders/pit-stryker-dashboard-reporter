package it.mulders.stryker.pitreporter;

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
import org.pitest.coverage.CoverageData;
import org.pitest.mutationtest.ClassMutationResults;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.MutationStatusTestPair;
import org.pitest.mutationtest.engine.Location;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.engine.MutationIdentifier;

import java.util.Collections;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StrykerDashboardMutationResultListenerTest implements WithAssertions {
    private final CodeSource codeSource = new CodeSource(
            new ProjectClassPaths(
                    new ClassPath(),
                    new ClassFilter(x -> true, x -> true),
                    new PathFilter(x -> true, x -> true)
            )
    );
    private final CoverageData coverage = new CoverageData(
            codeSource,
            null
    );
    private final StrykerDashboardMutationResultListener listener = new StrykerDashboardMutationResultListener(coverage);

    static class Demo {
        int add(int a, int b) {
            return a + b;
        }
    }

    private final MutationDetails mutationDetails = new MutationDetails(
            new MutationIdentifier(
                    new Location(ClassName.fromClass(Demo.class), "add", "add(int, int)/int"),
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
        var mstp = new MutationStatusTestPair(1, null, null, null);
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
}