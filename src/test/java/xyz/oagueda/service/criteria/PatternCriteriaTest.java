package xyz.oagueda.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PatternCriteriaTest {

    @Test
    void newPatternCriteriaHasAllFiltersNullTest() {
        var patternCriteria = new PatternCriteria();
        assertThat(patternCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void patternCriteriaFluentMethodsCreatesFiltersTest() {
        var patternCriteria = new PatternCriteria();

        setAllFilters(patternCriteria);

        assertThat(patternCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void patternCriteriaCopyCreatesNullFilterTest() {
        var patternCriteria = new PatternCriteria();
        var copy = patternCriteria.copy();

        assertThat(patternCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(patternCriteria)
        );
    }

    @Test
    void patternCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var patternCriteria = new PatternCriteria();
        setAllFilters(patternCriteria);

        var copy = patternCriteria.copy();

        assertThat(patternCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(patternCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var patternCriteria = new PatternCriteria();

        assertThat(patternCriteria).hasToString("PatternCriteria{}");
    }

    private static void setAllFilters(PatternCriteria patternCriteria) {
        patternCriteria.id();
        patternCriteria.code();
        patternCriteria.deviceId();
        patternCriteria.distinct();
    }

    private static Condition<PatternCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getDeviceId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PatternCriteria> copyFiltersAre(PatternCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getDeviceId(), copy.getDeviceId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
