package xyz.oagueda.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RepairCriteriaTest {

    @Test
    void newRepairCriteriaHasAllFiltersNullTest() {
        var repairCriteria = new RepairCriteria();
        assertThat(repairCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void repairCriteriaFluentMethodsCreatesFiltersTest() {
        var repairCriteria = new RepairCriteria();

        setAllFilters(repairCriteria);

        assertThat(repairCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void repairCriteriaCopyCreatesNullFilterTest() {
        var repairCriteria = new RepairCriteria();
        var copy = repairCriteria.copy();

        assertThat(repairCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(repairCriteria)
        );
    }

    @Test
    void repairCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var repairCriteria = new RepairCriteria();
        setAllFilters(repairCriteria);

        var copy = repairCriteria.copy();

        assertThat(repairCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(repairCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var repairCriteria = new RepairCriteria();

        assertThat(repairCriteria).hasToString("RepairCriteria{}");
    }

    private static void setAllFilters(RepairCriteria repairCriteria) {
        repairCriteria.id();
        repairCriteria.status();
        repairCriteria.closedDate();
        repairCriteria.budget();
        repairCriteria.importantData();
        repairCriteria.total();
        repairCriteria.deviceId();
        repairCriteria.distinct();
    }

    private static Condition<RepairCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getClosedDate()) &&
                condition.apply(criteria.getBudget()) &&
                condition.apply(criteria.getImportantData()) &&
                condition.apply(criteria.getTotal()) &&
                condition.apply(criteria.getDeviceId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RepairCriteria> copyFiltersAre(RepairCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getClosedDate(), copy.getClosedDate()) &&
                condition.apply(criteria.getBudget(), copy.getBudget()) &&
                condition.apply(criteria.getImportantData(), copy.getImportantData()) &&
                condition.apply(criteria.getTotal(), copy.getTotal()) &&
                condition.apply(criteria.getDeviceId(), copy.getDeviceId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
