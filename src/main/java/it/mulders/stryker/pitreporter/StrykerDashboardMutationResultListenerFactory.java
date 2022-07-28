package it.mulders.stryker.pitreporter;

import java.util.Properties;

import org.pitest.mutationtest.ListenerArguments;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.mutationtest.MutationResultListenerFactory;

/**
 * Factory for creating a {@link MutationResultListener} that can interact with the Stryker Dashboard.
 */
public class StrykerDashboardMutationResultListenerFactory implements MutationResultListenerFactory {
    /**
     * {@inheritDoc}
     */
    @Override
    public MutationResultListener getListener(final Properties props, final ListenerArguments args) {
        return new StrykerDashboardMutationResultListener(
                args.getCoverage(),
                args.getLocator()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() {
        return "stryker-dashboard";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String description() {
        return "Stryker Dashboard reporter for PIT";
    }
}
