package it.mulders.stryker.pitreporter;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.pitest.classpath.DefaultCodeSource;
import org.pitest.mutationtest.ListenerArguments;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.mutationtest.MutationResultListenerFactory;
import org.pitest.util.Log;

/**
 * Factory for creating a {@link MutationResultListener} that can interact with the Stryker Dashboard.
 */
public class StrykerDashboardMutationResultListenerFactory implements MutationResultListenerFactory {
    // Visible for testing
    static final String STRYKER_MODULE_NAME_PROPERTY = "stryker.moduleName";
    private static final Logger log = Log.getLogger();

    /**
     * {@inheritDoc}
     */
    @Override
    public MutationResultListener getListener(final Properties props, final ListenerArguments args) {
        var moduleName = props.getProperty(STRYKER_MODULE_NAME_PROPERTY);
        if (moduleName != null) {
            log.log(Level.INFO, "Detected module name {0}", moduleName);
        } else {
            log.log(Level.INFO, "No module name detected");
        }

        var codeSource = new DefaultCodeSource(args.data().getMutationClassPaths());

        return new StrykerDashboardMutationResultListener(
                codeSource,
                moduleName,
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
