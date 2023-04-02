package demo.reservation.service;

import demo.reservation.service.mechanics.MechanicsService;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.smallrye.common.constraint.Assert.assertFalse;
import static io.smallrye.common.constraint.Assert.assertTrue;

@QuarkusTest
class MechanicsIntegratonTest {
    @Inject
    MechanicsService mechanicsService;

    @Test
    void whenFindByCorrectName_thenMechanicsShouldBeFound() {
        assertTrue(mechanicsService.find("NAME", "Test mechanics").isPresent());
    }

    @Test
    void whenFindByWrongName_thenMechanicsShouldNotBeFound() {
        assertTrue(mechanicsService.find("NAME", "Wrong mechanics name").isEmpty());
    }
}
