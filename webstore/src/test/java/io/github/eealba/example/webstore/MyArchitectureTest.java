package io.github.eealba.example.webstore;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

@AnalyzeClasses(packages = "io.github.eealba.example.webstore")
public class MyArchitectureTest {
    @ArchTest
    static final ArchRule layer_dependencies_are_respected = layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .layer("core").definedBy("..core..")
            .layer("infrastructure").definedBy("..infrastructure..")
            .whereLayer("core").mayOnlyBeAccessedByLayers("infrastructure")
            .whereLayer("core").mayNotAccessAnyLayer()
            .whereLayer("infrastructure").mayNotBeAccessedByAnyLayer();


    @ArchTest
    private final ArchRule no_java_util_logging = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;


}
