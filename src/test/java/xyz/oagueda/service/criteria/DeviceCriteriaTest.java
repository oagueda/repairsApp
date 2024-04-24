package xyz.oagueda.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DeviceCriteriaTest {

    @Test
    void newDeviceCriteriaHasAllFiltersNullTest() {
        var deviceCriteria = new DeviceCriteria();
        assertThat(deviceCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void deviceCriteriaFluentMethodsCreatesFiltersTest() {
        var deviceCriteria = new DeviceCriteria();

        setAllFilters(deviceCriteria);

        assertThat(deviceCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void deviceCriteriaCopyCreatesNullFilterTest() {
        var deviceCriteria = new DeviceCriteria();
        var copy = deviceCriteria.copy();

        assertThat(deviceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(deviceCriteria)
        );
    }

    @Test
    void deviceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var deviceCriteria = new DeviceCriteria();
        setAllFilters(deviceCriteria);

        var copy = deviceCriteria.copy();

        assertThat(deviceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(deviceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var deviceCriteria = new DeviceCriteria();

        assertThat(deviceCriteria).hasToString("DeviceCriteria{}");
    }

    private static void setAllFilters(DeviceCriteria deviceCriteria) {
        deviceCriteria.id();
        deviceCriteria.type();
        deviceCriteria.brand();
        deviceCriteria.model();
        deviceCriteria.serialNumber();
        deviceCriteria.warranty();
        deviceCriteria.password();
        deviceCriteria.hasPattern();
        deviceCriteria.simPinCode();
        deviceCriteria.deleted();
        deviceCriteria.patternId();
        deviceCriteria.repairId();
        deviceCriteria.customerId();
        deviceCriteria.distinct();
    }

    private static Condition<DeviceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getBrand()) &&
                condition.apply(criteria.getModel()) &&
                condition.apply(criteria.getSerialNumber()) &&
                condition.apply(criteria.getWarranty()) &&
                condition.apply(criteria.getPassword()) &&
                condition.apply(criteria.getHasPattern()) &&
                condition.apply(criteria.getSimPinCode()) &&
                condition.apply(criteria.getDeleted()) &&
                condition.apply(criteria.getPatternId()) &&
                condition.apply(criteria.getRepairId()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DeviceCriteria> copyFiltersAre(DeviceCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getBrand(), copy.getBrand()) &&
                condition.apply(criteria.getModel(), copy.getModel()) &&
                condition.apply(criteria.getSerialNumber(), copy.getSerialNumber()) &&
                condition.apply(criteria.getWarranty(), copy.getWarranty()) &&
                condition.apply(criteria.getPassword(), copy.getPassword()) &&
                condition.apply(criteria.getHasPattern(), copy.getHasPattern()) &&
                condition.apply(criteria.getSimPinCode(), copy.getSimPinCode()) &&
                condition.apply(criteria.getDeleted(), copy.getDeleted()) &&
                condition.apply(criteria.getPatternId(), copy.getPatternId()) &&
                condition.apply(criteria.getRepairId(), copy.getRepairId()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
