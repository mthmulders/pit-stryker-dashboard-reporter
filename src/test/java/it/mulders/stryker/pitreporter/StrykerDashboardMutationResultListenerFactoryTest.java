package it.mulders.stryker.pitreporter;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.pitest.classinfo.ClassName;
import org.pitest.classpath.CodeSource;
import org.pitest.mutationtest.ListenerArguments;
import org.pitest.mutationtest.MutationResultListenerFactory;
import org.pitest.mutationtest.config.ReportOptions;

import java.util.Collections;
import java.util.Properties;

import static it.mulders.stryker.pitreporter.StrykerDashboardMutationResultListenerFactory.STRYKER_MODULE_NAME_PROPERTY;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

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
        var reportOptions = new ReportOptions();
        var args = new ListenerArguments(null, null, null, null, System.currentTimeMillis(), false, reportOptions, Collections.emptyList());

        // act
        var listener = factory.getListener(new Properties(), args);

        // assert
        assertThat(listener).isInstanceOf(StrykerDashboardMutationResultListener.class)
                .hasFieldOrPropertyWithValue("moduleName", null);
    }

    @Test
    void should_specify_module_name_if_supplied() {
        // arrange
        var props = new Properties();
        props.put(STRYKER_MODULE_NAME_PROPERTY, "example-1");
        var reportOptions = new ReportOptions();

        var args = new ListenerArguments(null, null, null, null, System.currentTimeMillis(), false, reportOptions, Collections.emptyList());

        // act
        var listener = factory.getListener(props, args);

        // assert
        assertThat(listener).isInstanceOf(StrykerDashboardMutationResultListener.class)
                .hasFieldOrPropertyWithValue("moduleName", "example-1");
    }

    @Test
    void should_have_code_source() {
        // arrange
        var props = new Properties();
        var reportOptions = new ReportOptions();
        reportOptions.setClassPathElements(Collections.singleton("target/test-classes"));
        reportOptions.setTargetTests(Collections.singleton(
                className -> true // Include all classes for simplicity
        ));

        var args = new ListenerArguments(null, null, null, null, System.currentTimeMillis(), false, reportOptions, Collections.emptyList());

        // act
        var listener = factory.getListener(props, args);

        // assert
        assertThat(listener).isInstanceOf(StrykerDashboardMutationResultListener.class)
                .extracting("codeSource")
                .asInstanceOf(type(CodeSource.class))
                .satisfies(codeSource -> {
                    assertThat(codeSource.getClassPath()).isNotNull();
                    var ownClassName = ClassName.fromClass(getClass());
                    assertThat(codeSource.getAllClassAndTestNames()).contains(ownClassName);
                });
    }
}