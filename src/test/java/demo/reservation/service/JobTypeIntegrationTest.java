package demo.reservation.service;

import demo.reservation.api.model.dto.JobTypeEnum;
import demo.reservation.service.jobtype.JobTypeService;
import io.quarkus.test.junit.QuarkusTest;
import org.hibernate.exception.SQLGrammarException;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import static io.smallrye.common.constraint.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class JobTypeIntegrationTest {
    @Inject
    JobTypeService jobTypeService;

    @Test
    void whenFindByJobType_thenJobTypeShouldBeFound() {
        assertFalse(jobTypeService.find("JOBTYPE", JobTypeEnum.TEST).isEmpty());
    }

    @Test
    void whenFindByWrongField_thenShouldThrowAnException() {
        assertThrows(SQLGrammarException.class, () ->
                jobTypeService.find("WRONG_FIELD", JobTypeEnum.TEST),
                "SQLGrammarException was expected");
    }
}
