package demo.reservation.architecture;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.Architectures;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.core.domain.properties.HasName.AndFullName.Predicates.fullNameMatching;
import static com.tngtech.archunit.core.domain.properties.HasName.Predicates.nameMatching;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@QuarkusTest
class ArchTest {
    JavaClasses javaClasses = new ClassFileImporter()
            .importPackages("demo.reservation");

    @Test
    void testArchitectureLayersLayer() {
        Architectures.LayeredArchitecture arch = layeredArchitecture()
                .consideringAllDependencies()
                // Define layers
                .layer("Presentation").definedBy("..api..")
                .layer("Service").definedBy("..service..")
                .layer("Persistence").definedBy("..persistence..")
                // Add constraints
                .whereLayer("Presentation").mayOnlyBeAccessedByLayers("Service", "Persistence")
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Presentation", "Persistence")
                .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service", "Presentation")
                .ignoreDependency(fullNameMatching(".*\\.ReservationUtil").or(nameMatching(".*\\.Controls\\$.*")), DescribedPredicate.alwaysTrue());
        arch.check(javaClasses);
    }
}
