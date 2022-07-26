package it.mulders.stryker.pitreporter;

import java.util.Properties;

import org.pitest.mutationtest.ListenerArguments;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.mutationtest.MutationResultListenerFactory;

public class StrykerDashboardMutationResultListenerFactory implements MutationResultListenerFactory {
    @Override
    public MutationResultListener getListener(final Properties props, final ListenerArguments args) {
        return new StrykerDashboardMutationResultListener(
                args.getCoverage(),
                args.getLocator()
        );
    }

    @Override
    public String name() {
        return "stryker-dashboard";
    }

    @Override
    public String description() {
        return "Stryker Dashboard reporter for PIT";
    }
}
