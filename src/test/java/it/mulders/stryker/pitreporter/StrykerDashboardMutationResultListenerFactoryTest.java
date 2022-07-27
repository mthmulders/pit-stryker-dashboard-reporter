package it.mulders.stryker.pitreporter;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.pitest.coverage.NoCoverage;
import org.pitest.mutationtest.ListenerArguments;
import org.pitest.mutationtest.MutationResultListenerFactory;

import java.util.Properties;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StrykerDashboardMutationResultListenerFactoryTest implements WithAssertions {
    private final MutationResultListenerFactory factory = new StrykerDashboardMutationResultListenerFactory();

    @Test
    void should_have_name() {
        assertThat(factory.name()).isEqualTo("stryker-dashboard");
    }

    @Test
    void should_have_description() {
        assertThat(factory.description()).isEqualTo("Stryker Dashboard reporter for PIT");
    }

    @Test
    void should_create_listener() {
        // arrange
        var coverage = new NoCoverage();
        var args = new ListenerArguments(null, coverage, null, null, System.currentTimeMillis(), false, null);

        // act
        var listener = factory.getListener(new Properties(), args);

        // assert
        assertThat(listener).isInstanceOf(StrykerDashboardMutationResultListener.class)
                .hasFieldOrPropertyWithValue("coverage", coverage);
    }
}